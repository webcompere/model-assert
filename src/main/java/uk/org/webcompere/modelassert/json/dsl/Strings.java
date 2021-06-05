package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

/**
 * Reduce the DSL down to just string nodes
 * @param <T> the type of source JSON
 * @param <A> the type of assertion
 */
public class Strings<T, A extends CoreJsonAssertion<T, A>> extends SubsetDsl<T, A> implements TextNodeDsl<T, A> {
    public Strings(Satisfies<T, A> satisfies) {
        super(satisfies);
    }
}
