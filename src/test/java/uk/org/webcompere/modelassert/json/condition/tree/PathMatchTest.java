package uk.org.webcompere.modelassert.json.condition.tree;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.webcompere.modelassert.json.dsl.nodespecific.tree.PathWildCard.ANY_FIELD;
import static uk.org.webcompere.modelassert.json.dsl.nodespecific.tree.PathWildCard.ANY_SUBTREE;

class PathMatchTest {

    @Test
    void pathMatchOfJsonPointerMatchesLocation() {
        assertThat(PathMatch.ofJsonPointer("/a/b/c")
            .matches(new Location().child("a").child("b").child("c"))).isTrue();
    }

    @Test
    void pathMatchOfJsonPointerDoesNotMatchDeeperLocation() {
        assertThat(PathMatch.ofJsonPointer("/a/b/c")
            .matches(new Location().child("a").child("b").child("c").child("d")))
            .isFalse();
    }

    @Test
    void subtreePathMatchCanFindNestedField() {
        assertThat(new PathMatch(ANY_SUBTREE, "d")
            .matches(new Location().child("a").child("b").child("c").child("d")))
            .isTrue();
    }

    @Test
    void subtreePathMatchCanFindNestedFieldPair() {
        assertThat(new PathMatch(ANY_SUBTREE, "c", "d")
            .matches(new Location().child("a").child("b").child("c").child("d")))
            .isTrue();
    }

    @Test
    void fieldWildCardCanApplyToAnywhereInPath() {
        assertThat(new PathMatch(ANY_FIELD, "b", "c")
            .matches(new Location().child("a").child("b").child("c")))
            .isTrue();

        assertThat(new PathMatch("a", ANY_FIELD, "c")
            .matches(new Location().child("a").child("b").child("c")))
            .isTrue();

        assertThat(new PathMatch("a", "b", ANY_FIELD)
            .matches(new Location().child("a").child("b").child("c")))
            .isTrue();
    }

    @Test
    void subtreePathMatchWontFindBeyondTargetpair() {
        assertThat(new PathMatch(ANY_SUBTREE, "c", "d")
            .matches(new Location().child("a").child("b").child("c").child("d").child("e")))
            .isFalse();
    }

    @Test
    void tailingSubTreeMatchesExactAndAllChildLocations() {
        PathMatch match = new PathMatch("a", "b", "c", ANY_SUBTREE);
        assertThat(match.matches(new Location().child("a").child("b").child("c")))
            .isTrue();

        assertThat(match.matches(new Location().child("a").child("b").child("c").child("d")))
            .isTrue();
    }

    @Test
    void prefixingAndTailingSubTreeMatchesExactAndAllChildLocations() {
        PathMatch match = new PathMatch(ANY_SUBTREE, "b", "c", ANY_SUBTREE);
        assertThat(match.matches(new Location().child("a").child("b").child("c")))
            .isTrue();

        assertThat(match.matches(new Location().child("a").child("b").child("c").child("d")))
            .isTrue();
    }
}
