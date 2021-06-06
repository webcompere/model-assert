package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import uk.org.webcompere.modelassert.json.condition.HasSize;
import uk.org.webcompere.modelassert.json.condition.IsEmpty;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

public interface Sizeable<T, A extends CoreJsonAssertion<T, A>> extends Satisfies<T, A> {
    /**
     * Depending on the type of node, this will detect <em>emptiness</em>
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A isEmpty() {
        return satisfies(new IsEmpty());
    }

    /**
     * Depending on the type of node, this will detect <em>size</em>
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default A hasSize(int expected) {
        return satisfies(new HasSize(expected));
    }
}
