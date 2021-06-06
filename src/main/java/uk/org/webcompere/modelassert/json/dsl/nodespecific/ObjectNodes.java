package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.SubsetDsl;

/**
 * Reduce the DSL down to just object
 * @param <A> the type of assertion
 */
public class ObjectNodes<A> extends SubsetDsl<A>
        implements ObjectNodeDsl<A> {
    public ObjectNodes(Satisfies<A> satisfies) {
        super(satisfies);
    }
}
