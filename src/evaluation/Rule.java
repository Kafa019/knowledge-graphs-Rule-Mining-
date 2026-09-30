package evaluation;

import java.util.ArrayList;
import java.util.List;

public class Rule {

    private Atom head;
    private final List<Atom> body;

    /*
     * Informazioni sulla qualità della regola.
     * Non tutti gli strumenti utilizzano
     * tutte queste informazioni.
     */

    private int support;
    private int correctPredictions;
    private double confidence;

    private double headCoverage;
    private double standardConfidence;
    private double pcaConfidence;

    public Rule() {
        body = new ArrayList<>();
    }

    /*
     * ==========================
     * HEAD
     * ==========================
     */

    public void setHead(Atom head) {
        this.head = head;
    }

    public Atom getHead() {
        return head;
    }

    /*
     * ==========================
     * BODY
     * ==========================
     */

    public void addBodyAtom(Atom atom) {
        body.add(atom);
    }

    public List<Atom> getBody() {
        return body;
    }

    /*
     * ==========================
     * ANYBURL
     * ==========================
     */

    public void setSupport(int support) {
        this.support = support;
    }

    public int getSupport() {
        return support;
    }

    public void setCorrectPredictions(int correctPredictions) {
        this.correctPredictions = correctPredictions;
    }

    public int getCorrectPredictions() {
        return correctPredictions;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getConfidence() {
        return confidence;
    }

    /*
     * ==========================
     * AMIE3
     * ==========================
     */

    public void setHeadCoverage(double headCoverage) {
        this.headCoverage = headCoverage;
    }

    public double getHeadCoverage() {
        return headCoverage;
    }

    public void setStandardConfidence(double standardConfidence) {
        this.standardConfidence = standardConfidence;
    }

    public double getStandardConfidence() {
        return standardConfidence;
    }

    public void setPcaConfidence(double pcaConfidence) {
        this.pcaConfidence = pcaConfidence;
    }

    public double getPcaConfidence() {
        return pcaConfidence;
    }

    /*
     * ==========================
     * STAMPA
     * ==========================
     */

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        if (head != null) {
            result.append(head);
        }

        if (!body.isEmpty()) {

            result.append(" <= ");

            for (int i = 0; i < body.size(); i++) {

                if (i > 0) {
                    result.append(", ");
                }

                result.append(body.get(i));
            }
        }

        return result.toString();
    }
}
