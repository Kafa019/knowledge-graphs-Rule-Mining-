package kg;
public class Triple {

    private String subject;
    private String predicate;
    private String object;

    // ORIGINAL, INVERSE oppure MINED
    private String source;

    public Triple(String subject,
                  String predicate,
                  String object,
                  String source) {

        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
        this.source = source;
    }

    public String getSubject() {
        return subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getObject() {
        return object;
    }

    public String getSource() {
        return source;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {

        return subject + "\t"
                + predicate + "\t"
                + object;

    }

}