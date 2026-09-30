package evaluation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AMIERuleParser implements RuleParser {

    @Override
    public List<Rule> parse(String fileName) throws IOException {

        List<Rule> rules = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileName),
                        StandardCharsets.UTF_16LE))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                // Ignora righe vuote
                if (line.isEmpty()) {
                    continue;
                }

                // Una regola AMIE deve contenere =>
                if (!line.contains("=>")) {
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

    private Rule parseRule(String line) {

        try {

            /*
             * Esempio reale AMIE:
             *
             * ?e  hasCategory3  ?b  ?e  hasDiagnosis  ?a
             * => ?a  belongsToCategory3  ?b
             *    1
             *    0,877192982
             *    ...
             */

            String[] parts = line.split("=>", 2);

            if (parts.length != 2) {
                return null;
            }

            String bodyText = parts[0].trim();

            String headAndMetrics = parts[1].trim();

            /*
             * La parte destra contiene:
             *
             * ?a belongsToCategory3 ?b
             *
             * seguita dalle metriche.
             *
             * Prendiamo solamente i primi 3 elementi.
             */

            String[] headTokens =
                    headAndMetrics.split("\\s+");

            if (headTokens.length < 3) {
                return null;
            }

            String headSubject = headTokens[0];
            String headPredicate = headTokens[1];
            String headObject = headTokens[2];

            /*
             * ATTENZIONE:
             * il costruttore Atom del nostro progetto è:
             *
             * Atom(predicate, subject, object)
             */

            Atom head = new Atom(
                    headPredicate,
                    headSubject,
                    headObject
            );

            Rule rule = new Rule();

            rule.setHead(head);

            /*
             * BODY
             *
             * AMIE utilizza due o più spazi tra gli atomi.
             *
             * Esempio:
             *
             * ?e hasCategory3 ?b
             * ?e hasDiagnosis ?a
             */

            String[] bodyTokens =
                    bodyText.split("\\s+");

            /*
             * Ogni atomo contiene esattamente:
             *
             * subject predicate object
             *
             * quindi 3 token.
             */

            if (bodyTokens.length < 3) {
                return null;
            }

            for (int i = 0;
                 i + 2 < bodyTokens.length;
                 i += 3) {

                String subject = bodyTokens[i];
                String predicate = bodyTokens[i + 1];
                String object = bodyTokens[i + 2];

                Atom atom = new Atom(
                        predicate,
                        subject,
                        object
                );

                rule.addBodyAtom(atom);
            }

            if (rule.getBody().isEmpty()) {
                return null;
            }

            return rule;

        } catch (Exception e) {

            return null;
        }
    }
}