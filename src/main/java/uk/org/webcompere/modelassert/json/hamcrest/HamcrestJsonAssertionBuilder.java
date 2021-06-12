package uk.org.webcompere.modelassert.json.hamcrest;

import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.JsonProvider;
import uk.org.webcompere.modelassert.json.dsl.JsonNodeAssertDsl;

/**
 * This is an interim object - the result of starting to build a hamcrest JSON assertion. It is not, itself
 * a hamcrest matcher, but will produce one as soon as a meaningful assertion is added
 * @param <T> the type of JSON that this asserts
 */
public class HamcrestJsonAssertionBuilder<T> implements JsonNodeAssertDsl<HamcrestJsonAssertion<T>> {
    private HamcrestJsonAssertion<T> assertion;

    public HamcrestJsonAssertionBuilder(JsonProvider<T> provider) {
        this.assertion = new HamcrestJsonAssertion<>(provider);
    }

    @Override
    public HamcrestJsonAssertion<T> satisfies(Condition condition) {
        return assertion.satisfies(condition);
    }
}
