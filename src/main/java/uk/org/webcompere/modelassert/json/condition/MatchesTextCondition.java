package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * A text node matches a regular expression
 */
public class MatchesTextCondition implements Condition {
    private Predicate<String> predicate;
    private String conditionName;

    /**
     * Create a condition which requires the text to meet the pattern
     * @param pattern regex that must match
     */
    public MatchesTextCondition(Pattern pattern) {
        this(pattern, "Text must match " + pattern.pattern());
    }

    /**
     * Create a condition which requires the text to meet the pattern
     * @param pattern regex that must match
     * @param conditionName the name of the condition for the description
     */
    public MatchesTextCondition(Pattern pattern, String conditionName) {
        this.predicate = text -> pattern.matcher(text).matches();
        this.conditionName = conditionName;
    }

    /**
     * Create a custom text condition
     * @param conditionName the name of the condition
     * @param predicate the predicate to meet on the text
     */
    public MatchesTextCondition(String conditionName, Predicate<String> predicate) {
        this.predicate = predicate;
        this.conditionName = conditionName;
    }

    /**
     * Build a text contains condition
     * @param contains the substring to search
     * @return a new {@link MatchesTextCondition} condition
     */
    public static MatchesTextCondition textContains(String contains) {
        return new MatchesTextCondition(Pattern.compile(".*" + Pattern.quote(contains) + ".*"),
            "Text contains " + contains);
    }

    /**
     * Execute the test of the condition
     *
     * @param json the json to test
     * @return a {@link Result} explaining whether the condition was met and if not, why not
     */
    @Override
    public Result test(JsonNode json) {
        if (!json.isTextual()) {
            return new Result(describe(), json.getNodeType().toString(), false);
        }

        String text = json.asText();
        return new Result(describe(), text, predicate.test(text));
    }

    /**
     * Describe the condition
     *
     * @return description of the condition
     */
    @Override
    public String describe() {
        return conditionName;
    }
}
