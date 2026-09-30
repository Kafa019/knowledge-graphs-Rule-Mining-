package kg;
import java.io.*;
import java.util.*;

public class DatasetSplitter {

    private static final double TRAIN = 0.80;
    private static final double VALID = 0.10;
    private static final double TEST = 0.10;

    public void split(List<Triple> triples) throws IOException {

        Map<String,List<Triple>> grouped = new HashMap<>();

        for(Triple t : triples){

            grouped.computeIfAbsent(
                    t.getSubject(),
                    k->new ArrayList<>()).add(t);

        }

        List<String> entities =
                new ArrayList<>(grouped.keySet());

        Collections.shuffle(entities,new Random(42));

        int trainSize =
                (int)(entities.size()*TRAIN);

        int validSize =
                (int)(entities.size()*VALID);

        Set<String> trainEntities =
                new HashSet<>();

        Set<String> validEntities =
                new HashSet<>();

        Set<String> testEntities =
                new HashSet<>();

        for(int i=0;i<entities.size();i++){

            if(i<trainSize){

                trainEntities.add(
                        entities.get(i));

            }
            else if(i<trainSize+validSize){

                validEntities.add(
                        entities.get(i));

            }
            else{

                testEntities.add(
                        entities.get(i));

            }

        }

        writeDataset(
                "train.txt",
                grouped,
                trainEntities);

        writeDataset(
                "valid.txt",
                grouped,
                validEntities);

        writeDataset(
                "test.txt",
                grouped,
                testEntities);

        System.out.println();

        System.out.println("Train : "
                + trainEntities.size());

        System.out.println("Valid : "
                + validEntities.size());

        System.out.println("Test  : "
                + testEntities.size());

    }

    private void writeDataset(
            String file,
            Map<String,List<Triple>> grouped,
            Set<String> entities)
            throws IOException{

        PrintWriter out =
                new PrintWriter(
                        new FileWriter(file));

        for(String entity : entities){

            List<Triple> list =
                    grouped.get(entity);

            if(list==null)
                continue;

            for(Triple t : list){

                out.println(
                        t.getSubject()
                        + "\t"
                        + t.getPredicate()
                        + "\t"
                        + t.getObject());

            }

        }

        out.close();

    }

}