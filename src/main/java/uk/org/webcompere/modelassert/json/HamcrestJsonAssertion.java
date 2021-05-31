package uk.org.webcompere.modelassert.json;

public class HamcrestJsonAssertion<T> extends JsonAssertion<T, HamcrestJsonAssertion<T>> {
    public HamcrestJsonAssertion(JsonProvider<T> jsonProvider) {
        super(jsonProvider);
    }
}
