package uk.org.webcompere.modelassert.json.hamcrest;

import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;
import uk.org.webcompere.modelassert.json.impl.JsonProvider;

/**
 * Add strong typing to the {@link CoreJsonAssertion} to provide a hamcrest only assertion
 * @param <T> the type of input json
 */
public class HamcrestJsonAssertion<T> extends CoreJsonAssertion<T, HamcrestJsonAssertion<T>> {
    public HamcrestJsonAssertion(JsonProvider<T> jsonProvider) {
        super(jsonProvider);
    }
}
