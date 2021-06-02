package uk.org.webcompere.modelassert.json.impl;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

import java.util.regex.Pattern;

/**
 * A text node matches a regular expression
 */
public class MatchesCondition implements Condition {
    private Pattern pattern;

    public MatchesCondition(Pattern pattern) {
        this.pattern = pattern;
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
            return new Result(describe(), json.toString(), false);
        }

        String text = json.asText();
        return new Result(describe(), text, pattern.matcher(text).matches());
    }

    /**
     * Describe the condition
     *
     * @return description of the condition
     */
    @Override
    public String describe() {
        return "Test must match " + pattern.pattern();
    }
}
