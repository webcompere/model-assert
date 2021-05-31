package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.JsonNode;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;

@FunctionalInterface
public interface JsonProvider<T> {
    JsonNode from(T json) throws IOException;

    @SuppressWarnings("unchecked") // hamcrest doesn't provide type safety but this object is constrained by generics
    default JsonNode jsonFrom(Object item) {
        try {
            return from((T)item);
        } catch (ClassCastException | IOException e) {
            throw new AssertionFailedError("Cannot read json of actual", e);
        }
    }
}
