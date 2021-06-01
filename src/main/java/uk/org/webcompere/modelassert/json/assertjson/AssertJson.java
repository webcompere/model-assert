package uk.org.webcompere.modelassert.json.assertjson;

import com.fasterxml.jackson.databind.JsonNode;
import org.opentest4j.AssertionFailedError;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.impl.JsonAssertion;
import uk.org.webcompere.modelassert.json.JsonProvider;
import uk.org.webcompere.modelassert.json.Result;

public class AssertJson<T> extends JsonAssertion<T, AssertJson<T>> {
    private JsonNode converted;

    public AssertJson(JsonProvider<T> jsonProvider, T source) {
        super(jsonProvider);

        converted = jsonProvider.jsonFrom(source);
    }

    @Override
    public AssertJson<T> satisfies(Condition condition) {
        // execute this comparison
        Result result = condition.test(converted);
        if (!result.isPassed()) {
            throw new AssertionFailedError("Expected: " + condition.describe() +
                    "\n     but: " + result.getExpected() + " was " + result.getWas(),
                    result.getExpected(), result.getWas());
        }

        return super.satisfies(condition);
    }
}
