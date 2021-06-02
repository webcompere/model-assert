package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.impl.*;

import java.util.regex.Pattern;

public class JsonAssertDslBuilders {

    /**
     * Builder of an <code>at</code> condition, for JSON Pointer comparison
     * @param <T> type of the input value to the assertion
     * @param <A> the type of assertion
     */
    public static class At<T, A extends CoreJsonAssertion<T, A>> {
        private CoreJsonAssertion<T, A> assertion;
        private String path;

        /**
         * Construct an at builder on a given assertion
         * @param assertion the assertion that will receive the at condition
         * @param path the JSON pointer to the path being asserted
         * @see {@link com.fasterxml.jackson.databind.JsonNode#at}
         */
        public At(CoreJsonAssertion<T, A> assertion, String path) {
            this.assertion = assertion;
            this.path = path;
        }

        /**
         * Assert that the path is equal to the given object
         * @param expected the expected
         * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
         */
        public CoreJsonAssertion<T, A> isEqualTo(Object expected) {
            return assertion.satisfies(new JsonAt(path, new IsEqualToObject(expected)));
        }

        /**
         * Assert that the path is null
         * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
         */
        public CoreJsonAssertion<T, A> isNull() {
            return assertion.satisfies(new JsonAt(path, NullCondition.getInstance()));
        }

        /**
         * Assert that the path is missing
         * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
         */
        public CoreJsonAssertion<T, A> isMissing() {
            return assertion.satisfies(new JsonAt(path, MissingCondition.getInstance()));
        }

        /**
         * Assert that the path matches a regular expression
         * @param regex the expression
         * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
         */
        public CoreJsonAssertion<T, A> matches(Pattern regex) {
            return assertion.satisfies(new JsonAt(path, new MatchesCondition(regex)));
        }

        /**
         * Assert that the path matches a regular expression
         * @param regex the expression
         * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
         */
        public CoreJsonAssertion<T, A> matches(String regex) {
            return assertion.satisfies(new JsonAt(path, new MatchesCondition(Pattern.compile(regex))));
        }
    }
}
