package evaluation;

import java.util.Objects;

public class Triple {

    private final String subject;
    private final String predicate;
    private final String object;

    public Triple(String subject, String predicate, String object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
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

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Triple)) {
            return false;
        }

        Triple other = (Triple) obj;

        return Objects.equals(subject, other.subject)
                && Objects.equals(predicate, other.predicate)
                && Objects.equals(object, other.object);
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                subject,
                predicate,
                object
        );
    }

    @Override
    public String toString() {

        return subject
                + " "
                + predicate
                + " "
                + object;
    }
}