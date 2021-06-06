package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.SubsetDsl;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

/**
 * Reduce the DSL down to just Aarrays
 * @param <T> the type of source JSON
 * @param <A> the type of assertion
 */
public class ArrayNodes<T, A extends CoreJsonAssertion<T, A>> extends SubsetDsl<T, A> implements ArrayNodeDsl<T, A> {
    public ArrayNodes(Satisfies<T, A> satisfies) {
        super(satisfies);
    }
}