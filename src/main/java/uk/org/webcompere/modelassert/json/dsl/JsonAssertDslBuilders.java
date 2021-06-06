package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.condition.JsonAt;

public class JsonAssertDslBuilders {

    /**
     * Builder of an <code>at</code> condition, for JSON Pointer comparison
     * @param <A> the type of assertion
     */
    public static class At<A> implements JsonNodeAssertDsl<A> {
        private Satisfies<A> satisfies;
        private String path;

        /**
         * Construct an at builder on a given assertion
         * @param satisfies the target for more conditions
         * @param path the JSON pointer to the path being asserted
         * @see com.fasterxml.jackson.databind.JsonNode#at
         */
        public At(Satisfies<A> satisfies, String path) {
            this.satisfies = satisfies;
            this.path = path;
        }

        @Override
        public A satisfies(Condition condition) {
            return satisfies.satisfies(new JsonAt(path, condition));
        }
    }
}
