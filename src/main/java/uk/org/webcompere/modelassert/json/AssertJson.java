package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.JsonNode;
import org.opentest4j.AssertionFailedError;

public class AssertJson<T> extends JsonAssertion<T, AssertJson<T>> {
    private JsonNode converted;

    public AssertJson(JsonProvider<T> jsonProvider, T source) {
        super(jsonProvider);

        converted = jsonProvider.jsonFrom(source);
    }

    @Override
    public AssertJson<T> plus(Comparison comparison) {
        // execute this comparison
        Result result = comparison.test(converted);
        if (!result.isPassed()) {
            throw new AssertionFailedError("Expected: " + comparison.describe() +
                    "\n     but: " + result.getExpected() + " was " + result.getWas(),
                    result.getExpected(), result.getWas());
        }

        return super.plus(comparison);
    }
}
