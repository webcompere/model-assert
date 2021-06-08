package uk.org.webcompere.modelassert.json.condition.tree;

import org.junit.jupiter.api.Test;
import uk.org.webcompere.modelassert.json.dsl.nodespecific.tree.PathWildCard;

import java.util.Collections;
import java.util.regex.Pattern;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.org.webcompere.modelassert.json.dsl.nodespecific.tree.PathWildCard.ANY_SUBTREE;

class PathMatcherTest {

    @Test
    void pathMatcherMustNotHaveAnyObjectInPath() {
        assertThatThrownBy(() -> PathMatcher.of(new Object()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void pathMatcherWithAnyFieldShouldMatchSingleLocation() {
        assertThat(PathMatcher.of(PathWildCard.ANY_FIELD)
            .matches(new Location().child("something"), Collections.emptyList()))
            .isTrue();
    }

    @Test
    void pathMatcherWithAnySubtreeShouldMatchSingleLocation() {
        assertThat(PathMatcher.of(ANY_SUBTREE)
            .matches(new Location().child("something"), Collections.emptyList()))
            .isTrue();
    }

    @Test
    void pathMatcherWithAnySubtreeShouldMatchNestedLocation() {
        assertThat(PathMatcher.of(ANY_SUBTREE)
            .matches(new Location().child("something").child("else"), Collections.emptyList()))
            .isTrue();
    }

    @Test
    void pathMatcherWithAnySubtreeShouldMatchRoot() {
        assertThat(PathMatcher.of(ANY_SUBTREE)
            .matches(new Location(), Collections.emptyList()))
            .isTrue();
    }

    @Test
    void pathMatcherWithAnySubtreeThenFieldShouldMatchNestedLocation() {
        assertThat(PathMatcher.of(ANY_SUBTREE)
            .matches(new Location().child("something").child("else"), singletonList(PathMatcher.of("else"))))
            .isTrue();
    }

    @Test
    void pathMatcherWithAnySubtreeThenFieldShouldNotMatchIncorrectNestedLocation() {
        assertThat(PathMatcher.of(ANY_SUBTREE)
            .matches(new Location().child("something").child("other"), singletonList(PathMatcher.of("else"))))
            .isFalse();
    }

    @Test
    void pathMatcherWithSpecificFieldShouldMatchSingleLocation() {
        assertThat(PathMatcher.of("something")
            .matches(new Location().child("something"), Collections.emptyList()))
            .isTrue();
    }

    @Test
    void pathMatcherWithRegexForFieldShouldMatchSingleLocation() {
        assertThat(PathMatcher.of(Pattern.compile("som.th..g"))
            .matches(new Location().child("something"), Collections.emptyList()))
            .isTrue();
    }

    @Test
    void subTreeBeforeTail() {
        assertThat(PathMatcher.of(ANY_SUBTREE)
            .matches(new Location().child("c"), singletonList(PathMatcher.of("c"))))
            .isTrue();
    }
}
