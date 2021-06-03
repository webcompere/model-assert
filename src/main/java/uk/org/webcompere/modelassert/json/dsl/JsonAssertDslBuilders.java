package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;
import uk.org.webcompere.modelassert.json.impl.JsonAt;

public class JsonAssertDslBuilders {

    /**
     * Builder of an <code>at</code> condition, for JSON Pointer comparison
     * @param <T> type of the input value to the assertion
     * @param <A> the type of assertion
     */
    public static class At<T, A extends CoreJsonAssertion<T, A>> implements JsonNodeAssertDsl<T, A> {
        private CoreJsonAssertion<T, A> assertion;
        private String path;

        /**
         * Construct an at builder on a given assertion
         * @param assertion the assertion that will receive the at condition
         * @param path the JSON pointer to the path being asserted
         * @see com.fasterxml.jackson.databind.JsonNode#at
         */
        public At(CoreJsonAssertion<T, A> assertion, String path) {
            this.assertion = assertion;
            this.path = path;
        }

        @Override
        public A satisfies(Condition condition) {
            return assertion.satisfies(new JsonAt(path, condition));
        }
    }
}
