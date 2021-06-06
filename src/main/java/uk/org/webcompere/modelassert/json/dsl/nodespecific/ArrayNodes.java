package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.SubsetDsl;

/**
 * Reduce the DSL down to just Aarrays
 * @param <A> the type of assertion
 */
public class ArrayNodes<A> extends SubsetDsl<A> implements ArrayNodeDsl<A> {
    public ArrayNodes(Satisfies<A> satisfies) {
        super(satisfies);
    }
}
