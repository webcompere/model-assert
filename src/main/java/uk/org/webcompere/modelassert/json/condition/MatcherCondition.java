package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

public class MatcherCondition implements Condition {
    private Matcher<JsonNode> matcher;

    public MatcherCondition(Matcher<JsonNode> matcher) {
        this.matcher = matcher;
    }

    /**
     * Execute the test of the condition
     *
     * @param json the json to test
     * @return a {@link Result} explaining whether the condition was met and if not, why not
     */
    @Override
    public Result test(JsonNode json) {
        boolean result = matcher.matches(json);
        StringDescription misMatchDescription = new StringDescription();
        if (!result) {
            matcher.describeMismatch(json, misMatchDescription);
        }
        return new Result(describe(), misMatchDescription.toString(), result);
    }

    /**
     * Describe the condition
     *
     * @return description of the condition
     */
    @Override
    public String describe() {
        StringDescription description = new StringDescription();
        matcher.describeTo(description);
        return description.toString();
    }
}
