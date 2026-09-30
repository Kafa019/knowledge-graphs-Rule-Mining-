package evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {

            System.out.println("======================================");
            System.out.println("       RULE EVALUATION TOOL");
            System.out.println("======================================");
            System.out.println();

            /*
             * ==========================================
             * SCELTA DELLO STRUMENTO
             * ==========================================
             */

            System.out.println("Seleziona il tipo di regole:");
            System.out.println();
            System.out.println("1 - AMIE3");
            System.out.println("2 - AnyBURL");
            System.out.println();

            int choice;

            while (true) {

                System.out.print("Scelta: ");

                String input =
                        scanner.nextLine().trim();

                try {

                    choice =
                            Integer.parseInt(input);

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Inserisci 1 oppure 2."
                    );

                    continue;
                }

                if (choice == 1
                        || choice == 2) {

                    break;
                }

                System.out.println(
                        "Scelta non valida."
                );
            }


            /*
             * ==========================================
             * SCELTA DEL PARSER
             * ==========================================
             */

            RuleParser parser;

            String toolName;

            if (choice == 1) {

                parser =
                        new AMIERuleParser();

                toolName =
                        "AMIE3";

            } else {

                parser =
                        new AnyBURLRuleParser();

                toolName =
                        "AnyBURL";
            }


            System.out.println();

            System.out.println(
                    "Parser selezionato: "
                    + toolName
            );


            /*
             * ==========================================
             * LETTURA TRAIN
             * ==========================================
             */

            System.out.println();

            System.out.println(
                    "Lettura train.txt..."
            );

            TripleReader tripleReader =
                    new TripleReader();

            Set<Triple> train =
                    tripleReader.read(
                            "train.txt"
                    );

            System.out.println(
                    "Train triples: "
                    + train.size()
            );


            /*
             * ==========================================
             * LETTURA TEST
             * ==========================================
             */

            System.out.println();

            System.out.println(
                    "Lettura test.txt..."
            );

            Set<Triple> test =
                    tripleReader.read(
                            "test.txt"
                    );

            System.out.println(
                    "Test triples: "
                    + test.size()
            );


            /*
             * ==========================================
             * LETTURA REGOLE
             * ==========================================
             */

            System.out.println();

            System.out.println(
                    "Lettura rules.txt..."
            );

            List<Rule> allRules =
                    parser.parse(
                            "rules.txt"
                    );

            System.out.println(
                    "Rules loaded: "
                    + allRules.size()
            );


            /*
             * Se non sono state trovate regole,
             * terminiamo il programma.
             */

            if (allRules.isEmpty()) {

                System.out.println();

                System.out.println(
                        "Nessuna regola caricata."
                );

                return;
            }


            /*
             * ==========================================
             * SCELTA DEL NUMERO DI REGOLE
             * ==========================================
             */

            System.out.println();

            System.out.println(
                    "Quante regole vuoi elaborare?"
            );

            System.out.println();

            System.out.println(
                    "1 - Prime 100 regole"
            );

            System.out.println(
                    "2 - Prime 1.000 regole"
            );

            System.out.println(
                    "3 - Prime 10.000 regole"
            );

            System.out.println(
                    "4 - Tutte le regole"
            );

            System.out.println();

            int ruleChoice;

            while (true) {

                System.out.print("Scelta: ");

                String input =
                        scanner.nextLine().trim();

                try {

                    ruleChoice =
                            Integer.parseInt(input);

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Inserisci un valore da 1 a 4."
                    );

                    continue;
                }

                if (ruleChoice >= 1
                        && ruleChoice <= 4) {

                    break;
                }

                System.out.println(
                        "Scelta non valida."
                );
            }


            /*
             * ==========================================
             * DETERMINAZIONE LIMITE REGOLE
             * ==========================================
             */

            int limit;

            switch (ruleChoice) {

                case 1:

                    limit = 100;

                    break;

                case 2:

                    limit = 1000;

                    break;

                case 3:

                    limit = 10000;

                    break;

                case 4:

                default:

                    limit =
                            allRules.size();

                    break;
            }


            /*
             * Se il numero richiesto è superiore
             * alle regole disponibili, utilizziamo
             * tutte quelle presenti.
             */

            limit =
                    Math.min(
                            limit,
                            allRules.size()
                    );


            /*
             * Creazione della lista delle regole
             * effettivamente elaborate.
             */

            List<Rule> rules =
                    new ArrayList<>(
                            allRules.subList(
                                    0,
                                    limit
                            )
                    );


            System.out.println();

            System.out.println(
                    "Regole da elaborare: "
                    + rules.size()
            );


            /*
             * ==========================================
             * ESECUZIONE DELLE REGOLE
             * ==========================================
             */

            System.out.println();

            System.out.println(
                    "Applicazione delle regole..."
            );

            long startTime =
                    System.currentTimeMillis();


            /*
             * IMPORTANTE:
             *
             * Passiamo anche il nome dello strumento
             * al RuleExecutor.
             *
             * AMIE3  -> variabili ?e ?a ?b
             * AnyBURL -> variabili X Y A B
             */

            RuleExecutor executor =
                    new RuleExecutor(
                            train,
                            toolName
                    );


            Set<Triple> generated =
                    executor.execute(
                            rules
                    );


            long endTime =
                    System.currentTimeMillis();


            double seconds =
                    (endTime - startTime)
                    / 1000.0;


            /*
             * ==========================================
             * RISULTATI GENERAZIONE
             * ==========================================
             */

            System.out.println();

            System.out.println(
                    "Generazione completata."
            );

            System.out.println(
                    "Generated triples: "
                    + generated.size()
            );

            System.out.printf(
                    "Execution time: %.2f seconds%n",
                    seconds
            );


            /*
             * ==========================================
             * VALUTAZIONE
             * ==========================================
             */

            System.out.println();

            System.out.println(
                    "Valutazione delle predizioni..."
            );


            Evaluator evaluator =
                    new Evaluator(
                            test
                    );


            evaluator.evaluate(
                    generated
            );


            evaluator.printReport(
                    generated.size()
            );


            /*
             * ==========================================
             * SALVATAGGIO RISULTATI
             * ==========================================
             */

            System.out.println();

            System.out.println(
                    "Vuoi salvare i risultati in un file TXT?"
            );

            System.out.println();

            System.out.println(
                    "1 - SI"
            );

            System.out.println(
                    "2 - NO"
            );

            System.out.println();

            int saveChoice;

            while (true) {

                System.out.print("Scelta: ");

                String input =
                        scanner.nextLine().trim();

                try {

                    saveChoice =
                            Integer.parseInt(input);

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Inserisci 1 oppure 2."
                    );

                    continue;
                }

                if (saveChoice == 1
                        || saveChoice == 2) {

                    break;
                }

                System.out.println(
                        "Scelta non valida."
                );
            }


            /*
             * ==========================================
             * SCRITTURA FILE
             * ==========================================
             */

            if (saveChoice == 1) {

                System.out.println();

                System.out.print(
                        "Nome del file [risultato.txt]: "
                );

                String fileName =
                        scanner.nextLine().trim();


                if (fileName.isEmpty()) {

                    fileName =
                            "risultato.txt";
                }


                if (!fileName
                        .toLowerCase()
                        .endsWith(".txt")) {

                    fileName += ".txt";
                }


                ReportWriter.write(
                        fileName,
                        toolName,
                        rules.size(),
                        train.size(),
                        test.size(),
                        generated.size(),
                        evaluator
                );


                System.out.println();

                System.out.println(
                        "Risultati salvati in: "
                        + fileName
                );
            }


            /*
             * ==========================================
             * FINE
             * ==========================================
             */

            System.out.println();

            System.out.println(
                    "Evaluation completed."
            );

        } catch (Exception e) {

            System.out.println();

            System.out.println(
                    "ERRORE DURANTE L'ESECUZIONE."
            );

            e.printStackTrace();

        } finally {

            scanner.close();
        }
    }
}