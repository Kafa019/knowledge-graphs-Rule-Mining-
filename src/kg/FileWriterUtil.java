package kg;
import java.io.*;
import java.util.*;

public class FileWriterUtil {

    public void writeTriples(
            List<Triple> triples,
            String fileName)
            throws IOException {

        PrintWriter out =
                new PrintWriter(
                        new BufferedWriter(
                                new FileWriter(fileName)));

        for (Triple t : triples) {

            out.println(
                    t.getSubject()
                            + "\t"
                            + t.getPredicate()
                            + "\t"
                            + t.getObject());

        }

        out.close();

        System.out.println();
        System.out.println(fileName
                + " creato.");

        System.out.println("Triple salvate: "
                + triples.size());

    }

    public void writeEntities(
            List<Triple> triples,
            String fileName)
            throws IOException {

        Set<String> entities =
                new TreeSet<>();

        for (Triple t : triples) {

            entities.add(t.getSubject());

            entities.add(t.getObject());

        }

        PrintWriter out =
                new PrintWriter(
                        new FileWriter(fileName));

        for (String entity : entities) {

            out.println(entity);

        }

        out.close();

        System.out.println(fileName
                + " creato.");

    }

    public void writeRelations(
            List<Triple> triples,
            String fileName)
            throws IOException {

        Set<String> relations =
                new TreeSet<>();

        for (Triple t : triples) {

            relations.add(
                    t.getPredicate());

        }

        PrintWriter out =
                new PrintWriter(
                        new FileWriter(fileName));

        for (String relation : relations) {

            out.println(relation);

        }

        out.close();

        System.out.println(fileName
                + " creato.");

    }

}