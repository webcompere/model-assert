package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.impl.JsonAssertion;
import uk.org.webcompere.modelassert.json.impl.JsonAt;

public class JsonAssertDslBuilders {

    /**
     * Builder of an <code>at</code> condition, for JSON Pointer comparison
     * @param <T> type of the input value to the assertion
     * @param <A> the type of assertion
     */
    public static class At<T, A extends JsonAssertion<T, A>> {
        private JsonAssertion<T, A> assertion;
        private String path;

        /**
         * Construct an at builder on a given assertion
         * @param assertion the assertion that will receive the at condition
         * @param path the JSON pointer to the path being asserted
         * @see {@link com.fasterxml.jackson.databind.JsonNode#at}
         */
        public At(JsonAssertion<T, A> assertion, String path) {
            this.assertion = assertion;
            this.path = path;
        }

        /**
         * Assert that the path is equal to the given object
         * @param expected the expected
         * @return the {@link JsonAssertion} for fluent assertions, with this condition added
         */
        public JsonAssertion<T, A> isEqualTo(Object expected) {
            return assertion.satisfies(new JsonAt(path, expected));
        }
    }
}
