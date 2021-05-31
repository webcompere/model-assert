package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonAssertDsl {
    static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static class At<T, J extends JsonAssertion<T, J>> {
        private JsonAssertion<T, J> assertion;
        private String path;

        public At(JsonAssertion<T, J> assertion, String path) {
            this.assertion = assertion;
            this.path = path;
        }

        public JsonAssertion<T, J> isEqualTo(Object value) {
            return assertion.plus(new JsonAt(path, value));
        }
    }
}
