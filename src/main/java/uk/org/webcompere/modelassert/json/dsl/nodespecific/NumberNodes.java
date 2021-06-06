package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.dsl.SubsetDsl;
import uk.org.webcompere.modelassert.json.dsl.nodespecific.NumberNodeDsl;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

/**
 * Reduce the DSL down to just numbers
 * @param <T> the type of source JSON
 * @param <A> the type of assertion
 */
public class NumberNodes<T, A extends CoreJsonAssertion<T, A>> extends SubsetDsl<T, A> implements NumberNodeDsl<T, A> {
    public NumberNodes(Satisfies<T, A> satisfies) {
        super(satisfies);
    }
}
