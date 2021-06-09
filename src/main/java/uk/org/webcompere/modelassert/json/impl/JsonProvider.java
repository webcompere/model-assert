package uk.org.webcompere.modelassert.json.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;

/**
 * Provides conversion to JSON of the input
 * @param <T> type of input
 */
@FunctionalInterface
public interface JsonProvider<T> {
    /**
     * Convert the input object into JSON
     * @param json the json source
     * @return a loaded {@link JsonNode} from the source
     * @throws IOException on load error
     */
    JsonNode from(T json) throws IOException;

    /**
     * Execute the provider, wrapping exceptions up as assertion failures
     * @param item the item to unpack - this will be of type <code>T</code>
     *             but we allow it to be <code>Object</code> as Hamcrest is poorly behaved with type safety
     * @return the loaded {@link JsonNode} or an assertion failure if the load failed, or the input is null
     */
    @SuppressWarnings("unchecked") // hamcrest doesn't provide type safety but this object is constrained by generics
    default JsonNode jsonFrom(Object item) {
        if (item == null) {
            return null;
        }
        try {
            return from((T)item);
        } catch (ClassCastException | IOException e) {
            throw new AssertionFailedError("Cannot read json of actual", e);
        }
    }
}
