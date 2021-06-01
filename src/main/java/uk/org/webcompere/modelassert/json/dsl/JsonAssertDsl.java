package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.impl.JsonAssertion;

public interface JsonAssertDsl<T, A extends JsonAssertion<T, A>> {

    A assertion();

    default JsonAssertDslBuilders.At<T, A> at(String path) {
        return new JsonAssertDslBuilders.At<T, A>(assertion(), path);
    }
}
