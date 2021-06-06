package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.SubsetDsl;
import uk.org.webcompere.modelassert.json.dsl.nodespecific.TextNodeDsl;

/**
 * Reduce the DSL down to just string nodes
 * @param <A> the type of assertion
 */
public class TextNodes<A> extends SubsetDsl<A> implements TextNodeDsl<A> {
    public TextNodes(Satisfies<A> satisfies) {
        super(satisfies);
    }
}
