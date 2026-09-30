package evaluation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ReportWriter {

    public static void write(
            String fileName,
            String toolName,
            int rulesLoaded,
            int trainSize,
            int testSize,
            int generatedTriples,
            Evaluator evaluator) throws IOException {

        try (PrintWriter writer =
                     new PrintWriter(
                             new FileWriter(fileName))) {

            writer.println("======================================");
            writer.println("       RULE EVALUATION REPORT");
            writer.println("======================================");

            writer.println();

            writer.println(
                    "Tool              : "
                    + toolName
            );

            writer.println(
                    "Rules loaded      : "
                    + rulesLoaded
            );

            writer.println(
                    "Train triples     : "
                    + trainSize
            );

            writer.println(
                    "Test triples      : "
                    + testSize
            );

            writer.println(
                    "Generated triples : "
                    + generatedTriples
            );

            writer.println();

            writer.println(
                    "True Positives    : "
                    + evaluator.getTruePositives()
            );

            writer.println(
                    "False Positives   : "
                    + evaluator.getFalsePositives()
            );

            writer.println(
                    "False Negatives   : "
                    + evaluator.getFalseNegatives()
            );

            writer.println();

            writer.printf(
                    "Precision         : %.6f%n",
                    evaluator.getPrecision()
            );

            writer.printf(
                    "Recall            : %.6f%n",
                    evaluator.getRecall()
            );

            writer.printf(
                    "F1-score          : %.6f%n",
                    evaluator.getF1Score()
            );

            writer.println();

            writer.println(
                    "======================================"
            );

            writer.println(
                    "Evaluation completed."
            );
        }
    }
}