package uk.org.webcompere.modelassert.json.dsl.nodespecific.tree;

import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.condition.Ignore;
import uk.org.webcompere.modelassert.json.condition.tree.PathMatch;
import uk.org.webcompere.modelassert.json.condition.tree.PathRule;
import uk.org.webcompere.modelassert.json.condition.tree.TreeRule;
import uk.org.webcompere.modelassert.json.dsl.JsonNodeAssertDsl;

import static uk.org.webcompere.modelassert.json.condition.tree.TreeRule.IGNORE_KEY_ORDER;

/**
 * Path DSL context within the {@link WhereDsl}
 * @param <A> the overall type of assertion
 */
public class PathDsl<A> implements JsonNodeAssertDsl<WhereDsl<A>> {
    private WhereDsl<A> whereDsl;
    private PathMatch pathMatch;

    PathDsl(WhereDsl<A> whereDsl, Object pathStart, Object... pathRemainder) {
        this.whereDsl = whereDsl;
        this.pathMatch = new PathMatch(pathStart, pathRemainder);
    }

    // overridden here to bridge back to the where dsl
    @Override
    public WhereDsl<A> satisfies(Condition condition) {
        return whereDsl.pathRule(new PathRule(pathMatch, condition));
    }

    /**
     * Add a {@link TreeRule#IGNORE_KEY_ORDER} to the where clause
     * @return the {@link WhereDsl} for fluent calling with the path added
     */
    public WhereDsl<A> keysInAnyOrder() {
        return whereDsl.pathRule(new PathRule(pathMatch, IGNORE_KEY_ORDER));
    }

    /**
     * Ignore everything at this path
     * @return the {@link WhereDsl} for fluent calling, with this path ignored
     */
    public WhereDsl<A> isIgnored() {
        return whereDsl.pathRule(new PathRule(pathMatch, new Ignore()));
    }
}
