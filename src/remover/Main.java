package remover;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Elenco dei file da ripulire dalle relazioni inverse
        String[] filesToProcess = {
            "knowledge_graph.txt",
            "train.txt",
            "valid.txt",
            "test.txt"
        };

        TripleReader reader = new TripleReader();
        TripleWriter writer = new TripleWriter();
        InverseRelationRemover remover = new InverseRelationRemover();

        System.out.println("====================================");
        System.out.println(" REMOVE INVERSE RELATIONS TOOL");
        System.out.println("====================================");

        for (String file : filesToProcess) {

            File f = new File(file);

            if (!f.exists()) {
                System.out.println("\n[SKIP] File non trovato: " + file);
                continue;
            }

            try {

                System.out.println("\nElaborazione file: " + file);

                List<Triple> triples = reader.read(file);

                List<Triple> filteredTriples = remover.removeInverseRelations(triples);

                // Sovrascrive il file originale senza le inverse
                writer.write(file, filteredTriples);

                System.out.println("File " + file + " aggiornato con successo.");

            } catch (IOException e) {

                System.out.println("Errore durante l'elaborazione del file: " + file);
                e.printStackTrace();

            }

        }

        System.out.println("\n====================================");
        System.out.println(" OPERAZIONE COMPLETATA");
        System.out.println("====================================");

    }

}