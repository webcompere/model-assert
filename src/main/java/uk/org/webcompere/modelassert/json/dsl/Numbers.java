package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

/**
 * Reduce the DSL down to just numbers
 * @param <T> the type of source JSON
 * @param <A> the type of assertion
 */
public class Numbers<T, A extends CoreJsonAssertion<T, A>> extends SubsetDsl<T, A> implements NumberNodeDsl<T, A> {
    public Numbers(Satisfies<T, A> satisfies) {
        super(satisfies);
    }
}
