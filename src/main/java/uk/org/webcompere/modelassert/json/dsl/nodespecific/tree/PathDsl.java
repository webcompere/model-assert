package uk.org.webcompere.modelassert.json.dsl.nodespecific.tree;

import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.condition.Ignore;
import uk.org.webcompere.modelassert.json.condition.tree.PathMatch;
import uk.org.webcompere.modelassert.json.condition.tree.PathRule;
import uk.org.webcompere.modelassert.json.condition.tree.TreeRule;
import uk.org.webcompere.modelassert.json.dsl.JsonNodeAssertDsl;

import static uk.org.webcompere.modelassert.json.condition.tree.TreeRule.*;

/**
 * Path DSL context within the {@link WhereDsl}
 * @param <A> the overall type of assertion
 */
public class PathDsl<A> implements JsonNodeAssertDsl<WhereDsl<A>> {
    private WhereDsl<A> whereDsl;
    private PathMatch pathMatch;

    PathDsl(WhereDsl<A> whereDsl, Object pathStart, Object... pathRemainder) {
        this(whereDsl, new PathMatch(pathStart, pathRemainder));
    }

    private PathDsl(WhereDsl<A> whereDsl, PathMatch pathMatch) {
        this.whereDsl = whereDsl;
        this.pathMatch = pathMatch;
    }

    /**
     * Create a path DSL from a JSON Pointer expression. Note: this allows for matching to the root
     * object with a path of <code>"/"</code>
     * @param where the parent where
     * @param jsonPointer the pointer expression
     * @param <A> the type of assertion
     * @return a new {@link PathDsl} for adding configuration to
     */
    public static <A> PathDsl<A> fromJsonPointer(WhereDsl<A> where, String jsonPointer) {
        return new PathDsl<>(where, PathMatch.ofJsonPointer(jsonPointer));
    }

    // overridden here to bridge back to the where dsl
    @Override
    public WhereDsl<A> satisfies(Condition condition) {
        return whereDsl.pathRule(new PathRule(pathMatch, condition));
    }

    /**
     * Add a {@link TreeRule#IGNORE_KEY_ORDER} to the clause
     * @return the {@link WhereDsl} for fluent calling with the path added
     */
    public WhereDsl<A> keysInAnyOrder() {
        return whereDsl.pathRule(new PathRule(pathMatch, IGNORE_KEY_ORDER));
    }

    /**
     * Add a {@link TreeRule#REQUIRE_KEY_ORDER} to the path - this can't be done in {@link WhereDsl}
     * as it is already the default so wouldn't mean anything
     * @return the {@link WhereDsl} for fluent calling with the path added
     */
    public WhereDsl<A> keysInOrder() {
        return whereDsl.pathRule(new PathRule(pathMatch, REQUIRE_KEY_ORDER));
    }

    /**
     * Allow missing keys in objects
     * @return the {@link WhereDsl} for fluent calling with the path added
     */
    public WhereDsl<A> objectContains() {
        return whereDsl.pathRule(new PathRule(pathMatch, OBJECT_CONTAINS));
    }

    /**
     * Relax the ordering requirement for an array at this position in the tree
     * @return <code>this</code> for fluent calling
     */
    public WhereDsl<A> arrayInAnyOrder() {
        return whereDsl.pathRule(new PathRule(pathMatch, TreeRule.IGNORE_ARRAY_ORDER));
    }

    /**
     * Allow the array at this position in the tree to just contain the other elements
     * rather than match it completely
     * @return <code>this</code> for fluent calling
     */
    public WhereDsl<A> arrayContains() {
        return whereDsl.pathRule(new PathRule(pathMatch, TreeRule.ARRAY_CONTAINS));
    }

    /**
     * Ignore everything at this path
     * @return the {@link WhereDsl} for fluent calling, with this path ignored
     */
    public WhereDsl<A> isIgnored() {
        return whereDsl.pathRule(new PathRule(pathMatch, new Ignore()));
    }
}
