package uk.org.webcompere.modelassert.json.hamcrest;

import uk.org.webcompere.modelassert.json.impl.JsonAssertion;
import uk.org.webcompere.modelassert.json.JsonProvider;

public class HamcrestJsonAssertion<T> extends JsonAssertion<T, HamcrestJsonAssertion<T>> {
    public HamcrestJsonAssertion(JsonProvider<T> jsonProvider) {
        super(jsonProvider);
    }
}
