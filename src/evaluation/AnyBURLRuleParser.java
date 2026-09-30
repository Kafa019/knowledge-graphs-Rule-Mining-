package evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnyBURLRuleParser implements RuleParser {

    @Override
    public List<Rule> parse(String fileName) throws IOException {

        List<Rule> rules = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                // Ignora righe vuote
                if (line.isEmpty()) {
                    continue;
                }

                Rule rule = parseRule(line);

                if (rule != null) {
                    rules.add(rule);
                }
            }
        }

        return rules;
    }

    /*
     * ==========================================
     * PARSING DI UNA REGOLA ANYBURL
     * ==========================================
     *
     * Formato:
     *
     * support correctPredictions confidence
     * HEAD <= BODY
     *
     * Esempio:
     *
     * 3840 40 0.0104
     * hasDiagnosis(?x,?y) <=
     * hasCategory3(?x,?z), ...
     *
     */

    private Rule parseRule(String line) {

        /*
         * Separiamo i primi tre valori numerici
         * dal resto della regola.
         *
         * Esempio:
         *
         * 3840 40 0.0104 hasDiagnosis(...)
         *
         * diventa:
         *
         * columns[0] = 3840
         * columns[1] = 40
         * columns[2] = 0.0104
         * columns[3] = hasDiagnosis(...)
         */

        String[] columns =
                line.split("\\s+", 4);

        if (columns.length < 4) {
            return null;
        }

        Rule rule = new Rule();

        /*
         * ==========================================
         * METRICHE ANYBURL
         * ==========================================
         */

        try {

            int support =
                    Integer.parseInt(columns[0]);

            int correctPredictions =
                    Integer.parseInt(columns[1]);

            double confidence =
                    Double.parseDouble(columns[2]);

            rule.setSupport(support);

            rule.setCorrectPredictions(
                    correctPredictions
            );

            rule.setConfidence(confidence);

        } catch (NumberFormatException e) {

            return null;
        }

        /*
         * ==========================================
         * SEPARAZIONE HEAD / BODY
         * ==========================================
         */

        String ruleText = columns[3];

        String[] parts =
                ruleText.split("\\s+<=\\s+", 2);

        if (parts.length != 2) {
            return null;
        }

        /*
         * ==========================================
         * HEAD
         * ==========================================
         */

        Atom head =
                parseAtom(parts[0].trim());

        if (head == null) {
            return null;
        }

        rule.setHead(head);

        /*
         * ==========================================
         * BODY
         * ==========================================
         */

        List<String> bodyAtoms =
                splitAtoms(parts[1].trim());

        for (String atomText : bodyAtoms) {

            Atom atom =
                    parseAtom(atomText);

            if (atom != null) {
                rule.addBodyAtom(atom);
            }
        }

        /*
         * Una regola senza body non è utile
         * per il nostro RuleExecutor.
         */

        if (rule.getBody().isEmpty()) {
            return null;
        }

        return rule;
    }

    /*
     * ==========================================
     * DIVISIONE DEGLI ATOMI DEL BODY
     * ==========================================
     *
     * Gli atomi sono separati da virgole.
     *
     * Esempio:
     *
     * A(?x,?y), B(?x,?z), C(?z,?y)
     *
     * diventa:
     *
     * A(?x,?y)
     * B(?x,?z)
     * C(?z,?y)
     *
     */

    private List<String> splitAtoms(String body) {

        List<String> atoms =
                new ArrayList<>();

        StringBuilder current =
                new StringBuilder();

        int parenthesesLevel = 0;

        for (int i = 0;
             i < body.length();
             i++) {

            char c = body.charAt(i);

            if (c == '(') {
                parenthesesLevel++;
            }

            else if (c == ')') {
                parenthesesLevel--;
            }

            /*
             * La virgola separa due atomi solo
             * quando non siamo dentro le parentesi.
             */

            if (c == ','
                    && parenthesesLevel == 0) {

                String atom =
                        current.toString().trim();

                if (!atom.isEmpty()) {
                    atoms.add(atom);
                }

                current.setLength(0);

            } else {

                current.append(c);
            }
        }

        String lastAtom =
                current.toString().trim();

        if (!lastAtom.isEmpty()) {
            atoms.add(lastAtom);
        }

        return atoms;
    }

    /*
     * ==========================================
     * PARSING DI UN ATOMO
     * ==========================================
     *
     * Formato:
     *
     * predicate(subject,object)
     *
     * Esempio:
     *
     * hasDiagnosis(?x,?y)
     *
     */

    private Atom parseAtom(String text) {

        text = text.trim();

        int open =
                text.indexOf('(');

        int close =
                text.lastIndexOf(')');

        if (open <= 0 || close <= open) {
            return null;
        }

        String predicate =
                text.substring(0, open).trim();

        String inside =
                text.substring(
                        open + 1,
                        close
                ).trim();

        /*
         * Cerchiamo la virgola che separa
         * soggetto e oggetto.
         */

        int comma =
                inside.indexOf(',');

        if (comma < 0) {
            return null;
        }

        String subject =
                inside.substring(
                        0,
                        comma
                ).trim();

        String object =
                inside.substring(
                        comma + 1
                ).trim();

        if (subject.isEmpty()
                || object.isEmpty()) {

            return null;
        }

        return new Atom(
                predicate,
                subject,
                object
        );
    }
}