package evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RuleExecutor {

    private final Set<Triple> train;
    private final String toolName;

    // Indici in sola lettura (thread-safe dopo la fase di build)
    private final Map<String, Set<Triple>> predicateIndex = new HashMap<>();
    private final Map<String, Map<String, Set<Triple>>> subjectIndex = new HashMap<>();
    private final Map<String, Map<String, Set<Triple>>> objectIndex = new HashMap<>();

    /*
     * RIUSO DEI BINDING (GC-Friendly):
     * Ogni thread riutilizza la propria istanza di Binding per evitare di 
     * allocare e deallocare memoria continuamente durante il backtracking.
     */
    private final ThreadLocal<Binding> threadLocalBinding = ThreadLocal.withInitial(Binding::new);

    public RuleExecutor(Set<Triple> train, String toolName) {
        this.train = train;
        this.toolName = toolName;
        buildIndexes();
    }

    private void buildIndexes() {
        System.out.println("Costruzione degli indici...");

        for (Triple triple : train) {
            String predicate = triple.getPredicate();
            String subject = triple.getSubject();
            String object = triple.getObject();

            predicateIndex
                    .computeIfAbsent(predicate, k -> new HashSet<>())
                    .add(triple);

            subjectIndex
                    .computeIfAbsent(predicate, k -> new HashMap<>())
                    .computeIfAbsent(subject, k -> new HashSet<>())
                    .add(triple);

            objectIndex
                    .computeIfAbsent(predicate, k -> new HashMap<>())
                    .computeIfAbsent(object, k -> new HashSet<>())
                    .add(triple);
        }

        System.out.println("Indici costruiti.");
    }

    /*
     * ESECUZIONE PARALLELA
     */
    public Set<Triple> execute(List<Rule> rules) {
        Set<Triple> generated = ConcurrentHashMap.newKeySet();
        AtomicInteger processedRules = new AtomicInteger(0);
        int totalRules = rules.size();

        // Esecuzione distribuita sul thread pool di Java
        rules.parallelStream().forEach(rule -> {
            executeRule(rule, generated);

            int current = processedRules.incrementAndGet();
            if (current % 100 == 0 || current == totalRules) {
                System.out.println(
                        "Regole elaborate: " + current + " / " + totalRules + 
                        " - Predizioni correnti: " + generated.size()
                );
            }
        });

        return generated;
    }

    private void executeRule(Rule rule, Set<Triple> generated) {
        if (rule == null || rule.getHead() == null || rule.getBody().isEmpty()) {
            return;
        }

        // Recupera il Binding riutilizzabile dedicato al thread corrente
        Binding binding = threadLocalBinding.get();
        binding.clear(); // Resetta lo stato del binding prima dell'uso

        solveBody(rule, 0, binding, generated);
    }

    private void solveBody(Rule rule, int atomIndex, Binding binding, Set<Triple> generated) {
        // CASO BASE: tutti gli atomi del body sono stati soddisfatti
        if (atomIndex >= rule.getBody().size()) {
            Triple prediction = instantiateHead(rule.getHead(), binding);
            if (prediction != null && !train.contains(prediction)) {
                generated.add(prediction);
            }
            return;
        }

        Atom atom = rule.getBody().get(atomIndex);

        String subject = resolveExistingValue(atom.getSubject(), binding);
        String object = resolveExistingValue(atom.getObject(), binding);

        Set<Triple> candidates = findCandidates(atom.getPredicate(), subject, object);

        if (candidates.isEmpty()) {
            return;
        }

        for (Triple triple : candidates) {
            List<String> addedVariables = new ArrayList<>(2);

            if (!bindTerm(atom.getSubject(), triple.getSubject(), binding, addedVariables)) {
                continue;
            }

            if (!bindTerm(atom.getObject(), triple.getObject(), binding, addedVariables)) {
                removeVariables(binding, addedVariables);
                continue;
            }

            // Ricorsione
            solveBody(rule, atomIndex + 1, binding, generated);

            // Backtrack: pulisce solo le variabili introdotte in questo branch
            removeVariables(binding, addedVariables);
        }
    }

    private Set<Triple> findCandidates(String predicate, String subject, String object) {
        if (subject != null) {
            Map<String, Set<Triple>> bySubject = subjectIndex.get(predicate);
            if (bySubject == null) return Collections.emptySet();
            Set<Triple> triples = bySubject.get(subject);
            return (triples != null) ? triples : Collections.emptySet();
        }

        if (object != null) {
            Map<String, Set<Triple>> byObject = objectIndex.get(predicate);
            if (byObject == null) return Collections.emptySet();
            Set<Triple> triples = byObject.get(object);
            return (triples != null) ? triples : Collections.emptySet();
        }

        Set<Triple> triples = predicateIndex.get(predicate);
        return (triples != null) ? triples : Collections.emptySet();
    }

    private String resolveExistingValue(String term, Binding binding) {
        if (!isVariable(term)) {
            return term;
        }
        if (binding.contains(term)) {
            return binding.get(term);
        }
        return null;
    }

    private boolean bindTerm(String term, String value, Binding binding, List<String> addedVariables) {
        if (!isVariable(term)) {
            return term.equals(value);
        }

        if (binding.contains(term)) {
            return value.equals(binding.get(term));
        }

        binding.put(term, value);
        addedVariables.add(term);
        return true;
    }

    private void removeVariables(Binding binding, List<String> variables) {
        for (String variable : variables) {
            binding.remove(variable);
        }
    }

    private Triple instantiateHead(Atom head, Binding binding) {
        String subject = resolveTerm(head.getSubject(), binding);
        String object = resolveTerm(head.getObject(), binding);

        if (subject == null || object == null) {
            return null;
        }

        return new Triple(subject, head.getPredicate(), object);
    }

    private String resolveTerm(String term, Binding binding) {
        if (isVariable(term)) {
            return binding.get(term);
        }
        return term;
    }

    private boolean isVariable(String term) {
        if ("AMIE3".equalsIgnoreCase(toolName)) {
            return isAMIEVariable(term);
        } else {
            return isAnyBURLVariable(term);
        }
    }

    private boolean isAMIEVariable(String term) {
        return term != null && term.length() > 1 && term.charAt(0) == '?';
    }

    private boolean isAnyBURLVariable(String term) {
        if (term == null || term.isEmpty()) {
            return false;
        }

        if (!Character.isUpperCase(term.charAt(0))) {
            return false;
        }

        for (int i = 1; i < term.length(); i++) {
            char c = term.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') {
                return false;
            }
        }
        return true;
    }
}