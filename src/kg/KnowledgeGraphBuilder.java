package kg;
import java.util.*;

public class KnowledgeGraphBuilder {

    private final List<Triple> triples = new ArrayList<>();

    public List<Triple> build(List<SkinCase> cases) {

        triples.clear();

        for (SkinCase sc : cases) {

            createDiagnosis(sc);

            createCategory3(sc);

            createCategory9(sc);

            createSkinType(sc);

            createFeatures(sc);

        }

        return triples;

    }

    private void createDiagnosis(SkinCase sc) {

        if (isEmpty(sc.getDiagnosis()))
            return;

        triples.add(new Triple(
                sc.getImageId(),
                "hasDiagnosis",
                normalize(sc.getDiagnosis()),
                "ORIGINAL"));

        triples.add(new Triple(
                normalize(sc.getDiagnosis()),
                "diagnosisOf",
                sc.getImageId(),
                "INVERSE"));

    }

    private void createCategory3(SkinCase sc) {

        if (isEmpty(sc.getCategory3()))
            return;

        triples.add(new Triple(
                sc.getImageId(),
                "hasCategory3",
                normalize(sc.getCategory3()),
                "ORIGINAL"));

        triples.add(new Triple(
                normalize(sc.getCategory3()),
                "category3Of",
                sc.getImageId(),
                "INVERSE"));

    }

    private void createCategory9(SkinCase sc) {

        if (isEmpty(sc.getCategory9()))
            return;

        triples.add(new Triple(
                sc.getImageId(),
                "hasCategory9",
                normalize(sc.getCategory9()),
                "ORIGINAL"));

        triples.add(new Triple(
                normalize(sc.getCategory9()),
                "category9Of",
                sc.getImageId(),
                "INVERSE"));
    }

    private void createSkinType(SkinCase sc) {

        if (isEmpty(sc.getSkinType()))
            return;

        String skin = "Type_" + normalize(sc.getSkinType());

        triples.add(new Triple(
                sc.getImageId(),
                "hasSkinType",
                skin,
                "ORIGINAL"));

        triples.add(new Triple(
                skin,
                "skinTypeOf",
                sc.getImageId(),
                "INVERSE"));

    }

    private void createFeatures(SkinCase sc) {

        for (String feature : sc.getFeatures()) {

            String f = normalize(feature);

            triples.add(new Triple(
                    sc.getImageId(),
                    "hasFeature",
                    f,
                    "ORIGINAL"));

            triples.add(new Triple(
                    f,
                    "featureOf",
                    sc.getImageId(),
                    "INVERSE"));

        }

    }

    private boolean isEmpty(String s) {

        return s == null || s.trim().isEmpty();

    }

    private String normalize(String value) {

        value = value.trim();

        value = value.replace(" ", "_");

        value = value.replace("-", "_");

        value = value.replace("/", "_");

        value = value.replace("(", "");

        value = value.replace(")", "");

        value = value.replace(",", "");

        value = value.replace(".", "");

        value = value.replace(":", "");

        return value;

    }

}