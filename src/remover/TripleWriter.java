package remover;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TripleWriter {

    public void write(String fileName,
                      List<Triple> triples) throws IOException {

        BufferedWriter writer =
                new BufferedWriter(
                        new FileWriter(fileName));

        for (Triple triple : triples) {

            writer.write(triple.toString());
            writer.newLine();

        }

        writer.close();

    }

}