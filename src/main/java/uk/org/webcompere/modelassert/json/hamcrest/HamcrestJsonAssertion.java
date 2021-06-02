package uk.org.webcompere.modelassert.json.hamcrest;

import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;
import uk.org.webcompere.modelassert.json.JsonProvider;

public class HamcrestJsonAssertion<T> extends CoreJsonAssertion<T, HamcrestJsonAssertion<T>> {
    public HamcrestJsonAssertion(JsonProvider<T> jsonProvider) {
        super(jsonProvider);
    }
}
