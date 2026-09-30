package kg;
import java.io.*;
import java.util.*;

public class Statistics {

    public void generate(List<SkinCase> cases,
                         List<Triple> triples) throws IOException {

        PrintWriter out =
                new PrintWriter(new FileWriter("statistics.txt"));

        Set<String> entities = new HashSet<>();
        Set<String> predicates = new HashSet<>();

        int original = 0;
        int inverse = 0;
        int mined = 0;

        Map<String,Integer> diagnosisFreq = new HashMap<>();
        Map<String,Integer> featureFreq = new HashMap<>();
        Map<String,Integer> predicateFreq = new HashMap<>();

        int totalFeatures = 0;

        for(SkinCase sc : cases){

            entities.add(sc.getImageId());

            if(sc.getDiagnosis()!=null){

                entities.add(sc.getDiagnosis());

                diagnosisFreq.put(
                        sc.getDiagnosis(),
                        diagnosisFreq.getOrDefault(
                                sc.getDiagnosis(),0)+1);

            }

            if(sc.getCategory3()!=null)
                entities.add(sc.getCategory3());

            if(sc.getCategory9()!=null)
                entities.add(sc.getCategory9());

            if(sc.getSkinType()!=null)
                entities.add(sc.getSkinType());

            totalFeatures += sc.getFeatures().size();

            for(String feature : sc.getFeatures()){

                entities.add(feature);

                featureFreq.put(
                        feature,
                        featureFreq.getOrDefault(
                                feature,0)+1);

            }

        }

        for(Triple t : triples){

            predicates.add(t.getPredicate());

            predicateFreq.put(
                    t.getPredicate(),
                    predicateFreq.getOrDefault(
                            t.getPredicate(),0)+1);

            switch(t.getSource()){

                case "ORIGINAL":

                    original++;

                    break;

                case "INVERSE":

                    inverse++;

                    break;

                case "MINED":

                    mined++;

                    break;

            }

        }

        out.println("=======================================");
        out.println(" KNOWLEDGE GRAPH STATISTICS");
        out.println("=======================================");
        out.println();

        out.println("Numero immagini.............. "
                + cases.size());

        out.println("Numero entita................. "
                + entities.size());

        out.println("Numero relazioni.............. "
                + predicates.size());

        out.println();

        out.println("Triple ORIGINAL............... "
                + original);

        out.println("Triple INVERSE................ "
                + inverse);

        out.println("Triple MINED.................. "
                + mined);

        out.println("Triple TOTALI................. "
                + triples.size());

        out.println();

        out.println("Diagnosi differenti........... "
                + diagnosisFreq.size());

        out.println("Feature differenti............ "
                + featureFreq.size());

        out.println();

        out.printf(Locale.US,
                "Media feature/immagine........ %.2f%n",
                (double)totalFeatures/cases.size());

        out.printf(Locale.US,
                "Media triple/immagine......... %.2f%n",
                (double)triples.size()/cases.size());

        out.println();

        out.println("Relazione più frequente....... "
                + max(predicateFreq));

        out.println("Diagnosi più frequente........ "
                + max(diagnosisFreq));

        out.println("Feature più frequente......... "
                + max(featureFreq));

        out.close();

        System.out.println("Creato statistics.txt");

    }

    private String max(Map<String,Integer> map){

        String best = "";

        int max = -1;

        for(String key : map.keySet()){

            if(map.get(key)>max){

                max = map.get(key);

                best = key;

            }

        }

        return best + " (" + max + ")";

    }

}