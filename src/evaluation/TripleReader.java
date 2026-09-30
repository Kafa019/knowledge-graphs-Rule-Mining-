package evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TripleReader {

    public Set<Triple> read(String fileName) throws IOException {

        Set<Triple> triples = new HashSet<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                line = line.trim();

                // Ignora righe vuote
                if (line.isEmpty()) {
                    continue;
                }

                /*
                 * I dataset utilizzano tre colonne:
                 *
                 * subject    predicate    object
                 *
                 * separate da tabulazioni.
                 *
                 * \\s+ permette comunque di gestire
                 * più spazi o tabulazioni.
                 */

                String[] parts = line.split("\\s+");

                if (parts.length < 3) {
                    System.err.println(
                            "Riga non valida in "
                            + fileName
                            + " alla riga "
                            + lineNumber
                    );

                    continue;
                }

                String subject = parts[0];
                String predicate = parts[1];
                String object = parts[2];

                Triple triple =
                        new Triple(
                                subject,
                                predicate,
                                object
                        );

                triples.add(triple);
            }
        }

        return triples;
    }
}