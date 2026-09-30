package remover;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InverseRelationRemover {

    /*
     * Tutte e 5 le relazioni inverse da eliminare dal Knowledge Graph
     */
    private Set<String> inverseRelations;

    public InverseRelationRemover() {

        inverseRelations = new HashSet<>();

        inverseRelations.add("diagnosisOf");
        inverseRelations.add("category3Of");
        inverseRelations.add("category9Of");
        inverseRelations.add("skinTypeOf");
        inverseRelations.add("featureOf"); // Aggiunta la quinta relazione inversa

    }

    public List<Triple> removeInverseRelations(List<Triple> triples) {

        List<Triple> filteredTriples = new ArrayList<>();

        int removed = 0;

        for (Triple triple : triples) {

            if (inverseRelations.contains(triple.getPredicate())) {

                removed++;

            } else {

                filteredTriples.add(triple);

            }

        }

        System.out.println();
        System.out.println("==================================");
        System.out.println(" INVERSE RELATION REMOVER");
        System.out.println("==================================");
        System.out.println();

        System.out.println("Triples read      : " + triples.size());
        System.out.println("Triples removed   : " + removed);
        System.out.println("Triples remaining : " + filteredTriples.size());

        return filteredTriples;

    }

}