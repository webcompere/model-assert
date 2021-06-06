package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.SubsetDsl;

/**
 * Reduce the DSL down to just booleans
 * @param <A> the type of assertion
 */
public class BooleanNodes<A> extends SubsetDsl<A> implements BooleanNodeDsl<A> {
    public BooleanNodes(Satisfies<A> satisfies) {
        super(satisfies);
    }
}
