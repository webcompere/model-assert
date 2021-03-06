package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import uk.org.webcompere.modelassert.json.condition.HasSize;
import uk.org.webcompere.modelassert.json.condition.IsEmpty;
import uk.org.webcompere.modelassert.json.dsl.Satisfies;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

public interface Sizeable<A> extends Satisfies<A> {
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

    /**
     * Requiring this node to be a sizeable node, this switches to {@link NumberComparisonDsl}
     * to allow criteria to be specified for the size.
     * @return the {@link CoreJsonAssertion} for fluent assertions, with this condition added
     */
    default NumberComparisonDsl<A> size() {
        return HasSize.sizeOf(this);
    }
}
