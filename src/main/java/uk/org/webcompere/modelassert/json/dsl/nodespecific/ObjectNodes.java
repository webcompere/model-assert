package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.SubsetDsl;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

/**
 * Reduce the DSL down to just object
 * @param <T> the type of source JSON
 * @param <A> the type of assertion
 */
public class ObjectNodes<T, A extends CoreJsonAssertion<T, A>> extends SubsetDsl<T, A>
        implements ObjectNodeDsl<T, A> {
    public ObjectNodes(Satisfies<T, A> satisfies) {
        super(satisfies);
    }
}
