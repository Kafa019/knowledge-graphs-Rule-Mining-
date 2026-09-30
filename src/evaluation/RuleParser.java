package evaluation;

import java.io.IOException;
import java.util.List;

public interface RuleParser {

    List<Rule> parse(String fileName) throws IOException;
}