package remover;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TripleReader {

    public List<Triple> read(String fileName) throws IOException {

        List<Triple> triples = new ArrayList<>();

        BufferedReader reader =
                new BufferedReader(
                        new FileReader(fileName));

        String line;

        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.isEmpty())
                continue;

            String[] parts = line.split("\\t");

            if (parts.length != 3)
                continue;

            Triple triple =
                    new Triple(
                            parts[0],
                            parts[1],
                            parts[2]);

            triples.add(triple);

        }

        reader.close();

        return triples;

    }

}