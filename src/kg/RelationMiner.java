package kg;
import java.util.*;

public class RelationMiner {

    // Una feature deve comparire almeno nell'80% dei casi
    private static final double FEATURE_THRESHOLD = 0.80;

    public void mine(List<SkinCase> cases, List<Triple> triples) {

        Map<String, Integer> diagnosisCount = new HashMap<>();

        Map<String, Map<String, Integer>> featureFrequency = new HashMap<>();

        for (SkinCase sc : cases) {

            String diagnosis = normalize(sc.getDiagnosis());

            diagnosisCount.put(diagnosis,
                    diagnosisCount.getOrDefault(diagnosis, 0) + 1);

            Map<String, Integer> map =
                    featureFrequency.computeIfAbsent(
                            diagnosis,
                            k -> new HashMap<>());

            for (String feature : sc.getFeatures()) {

                feature = normalize(feature);

                map.put(feature,
                        map.getOrDefault(feature, 0) + 1);

            }

        }

        createDiagnosisFeatureRelations(
                diagnosisCount,
                featureFrequency,
                triples);

        createCategoryRelations(cases, triples);

    }

    private void createDiagnosisFeatureRelations(
            Map<String, Integer> diagnosisCount,
            Map<String, Map<String, Integer>> featureFrequency,
            List<Triple> triples) {

        for (String diagnosis : featureFrequency.keySet()) {

            int total = diagnosisCount.get(diagnosis);

            Map<String, Integer> map =
                    featureFrequency.get(diagnosis);

            for (String feature : map.keySet()) {

                int count = map.get(feature);

                double ratio = (double) count / total;

                if (ratio >= FEATURE_THRESHOLD) {

                    triples.add(new Triple(
                            diagnosis,
                            "associatedWithFeature",
                            feature,
                            "MINED"));

                    triples.add(new Triple(
                            feature,
                            "indicatesDiagnosis",
                            diagnosis,
                            "MINED"));

                }

            }

        }

    }

    private void createCategoryRelations(
            List<SkinCase> cases,
            List<Triple> triples) {

        Set<String> done = new HashSet<>();

        for (SkinCase sc : cases) {

            if (sc.getDiagnosis() == null)
                continue;

            if (sc.getCategory3() == null)
                continue;

            String key =
                    sc.getDiagnosis() + "|" + sc.getCategory3();

            if (done.contains(key))
                continue;

            done.add(key);

            triples.add(new Triple(
                    normalize(sc.getDiagnosis()),
                    "belongsToCategory3",
                    normalize(sc.getCategory3()),
                    "MINED"));

        }

        done.clear();

        for (SkinCase sc : cases) {

            if (sc.getDiagnosis() == null)
                continue;

            if (sc.getCategory9() == null)
                continue;

            String key =
                    sc.getDiagnosis() + "|" + sc.getCategory9();

            if (done.contains(key))
                continue;

            done.add(key);

            triples.add(new Triple(
                    normalize(sc.getDiagnosis()),
                    "belongsToCategory9",
                    normalize(sc.getCategory9()),
                    "MINED"));
        }

    }

    private String normalize(String value) {

        if (value == null)
            return "";

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