package uk.org.webcompere.modelassert.json.assertjson;

import com.fasterxml.jackson.databind.JsonNode;
import org.opentest4j.AssertionFailedError;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;
import uk.org.webcompere.modelassert.json.impl.JsonProvider;

/**
 * The assertJson form of the assertion
 * @param <T> the type of JSON source passed in
 */
public class AssertJson<T> extends CoreJsonAssertion<T, AssertJson<T>> {
    private JsonNode converted;

    /**
     * Constructs an assertion - use the <code>assertJson</code> factory method instead
     * @param jsonProvider the provider of the json, converting from source type to {@link JsonNode}
     * @param source the source value
     */
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
                    "\n     but: " + result.getCondition() + " was " + result.getWas(),
                    result.getCondition(), result.getWas());
        }

        return super.satisfies(condition);
    }
}
