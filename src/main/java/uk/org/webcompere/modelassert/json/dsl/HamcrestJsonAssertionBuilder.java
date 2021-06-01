package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.hamcrest.HamcrestJsonAssertion;
import uk.org.webcompere.modelassert.json.JsonProvider;

public class HamcrestJsonAssertionBuilder<T> implements JsonAssertDsl<T, HamcrestJsonAssertion<T>> {
    private HamcrestJsonAssertion<T> assertion;

    public HamcrestJsonAssertionBuilder(JsonProvider<T> provider) {
        this.assertion = new HamcrestJsonAssertion<>(provider);
    }

    @Override
    public HamcrestJsonAssertion<T> assertion() {
        return assertion;
    }
}
