package evaluation;

import java.util.Set;

public class Evaluator {

    private final Set<Triple> testTriples;

    private int truePositives;
    private int falsePositives;
    private int falseNegatives;

    private double precision;
    private double recall;
    private double f1Score;

    public Evaluator(Set<Triple> testTriples) {

        this.testTriples = testTriples;
    }

    /*
     * ==========================================
     * VALUTAZIONE
     * ==========================================
     */

    public void evaluate(Set<Triple> predictions) {

        truePositives = 0;
        falsePositives = 0;
        falseNegatives = 0;

        /*
         * ======================================
         * TRUE POSITIVES / FALSE POSITIVES
         * ======================================
         */

        for (Triple prediction : predictions) {

            if (testTriples.contains(prediction)) {

                truePositives++;

            } else {

                falsePositives++;
            }
        }

        /*
         * ======================================
         * FALSE NEGATIVES
         * ======================================
         *
         * Triple presenti nel test ma che
         * non sono state generate.
         */

        for (Triple testTriple : testTriples) {

            if (!predictions.contains(testTriple)) {

                falseNegatives++;
            }
        }

        /*
         * ======================================
         * METRICHE
         * ======================================
         */

        calculateMetrics();
    }

    /*
     * ==========================================
     * CALCOLO DELLE METRICHE
     * ==========================================
     */

    private void calculateMetrics() {

        /*
         * Precision
         *
         * TP / (TP + FP)
         */

        if (truePositives + falsePositives > 0) {

            precision =
                    (double) truePositives
                    / (truePositives + falsePositives);

        } else {

            precision = 0.0;
        }

        /*
         * Recall
         *
         * TP / (TP + FN)
         */

        if (truePositives + falseNegatives > 0) {

            recall =
                    (double) truePositives
                    / (truePositives + falseNegatives);

        } else {

            recall = 0.0;
        }

        /*
         * F1-score
         *
         * 2 * Precision * Recall
         * ---------------------
         * Precision + Recall
         */

        if (precision + recall > 0) {

            f1Score =
                    2.0
                    * precision
                    * recall
                    / (precision + recall);

        } else {

            f1Score = 0.0;
        }
    }

    /*
     * ==========================================
     * GETTER
     * ==========================================
     */

    public int getTruePositives() {
        return truePositives;
    }

    public int getFalsePositives() {
        return falsePositives;
    }

    public int getFalseNegatives() {
        return falseNegatives;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public double getF1Score() {
        return f1Score;
    }

    /*
     * ==========================================
     * NUMERO DI TRIPLE DEL TEST
     * ==========================================
     */

    public int getTestSize() {
        return testTriples.size();
    }

    /*
     * ==========================================
     * REPORT
     * ==========================================
     */

    public void printReport(int generatedTriples) {

        System.out.println();
        System.out.println("======================================");
        System.out.println("          EVALUATION RESULTS");
        System.out.println("======================================");

        System.out.println();

        System.out.println(
                "Generated triples : "
                + generatedTriples
        );

        System.out.println(
                "Test triples      : "
                + testTriples.size()
        );

        System.out.println();

        System.out.println(
                "True Positives    : "
                + truePositives
        );

        System.out.println(
                "False Positives   : "
                + falsePositives
        );

        System.out.println(
                "False Negatives   : "
                + falseNegatives
        );

        System.out.println();

        System.out.printf(
                "Precision         : %.6f%n",
                precision
        );

        System.out.printf(
                "Recall            : %.6f%n",
                recall
        );

        System.out.printf(
                "F1-score          : %.6f%n",
                f1Score
        );

        System.out.println(
                "======================================"
        );
    }
}
