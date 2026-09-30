package evaluation;

public class Atom {

    private final String predicate;
    private final String subject;
    private final String object;

    public Atom(String predicate, String subject, String object) {
        this.predicate = predicate;
        this.subject = subject;
        this.object = object;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getSubject() {
        return subject;
    }

    public String getObject() {
        return object;
    }

    @Override
    public String toString() {

        return predicate
                + "("
                + subject
                + ","
                + object
                + ")";
    }
}