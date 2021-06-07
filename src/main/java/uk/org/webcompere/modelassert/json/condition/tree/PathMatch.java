package uk.org.webcompere.modelassert.json.condition.tree;

import uk.org.webcompere.modelassert.json.dsl.nodespecific.tree.PathWildCard;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Describes how a path may be matched
 */
public class PathMatch {
    private List<PathMatcher> matchers;

    /**
     * Constructed with at least one value which may be {@link String}, {@link Pattern} or {@link PathWildCard}
     * @param pathStart first part of the path expression
     * @param pathRemainder remaining elements of the path expression
     */
    public PathMatch(Object pathStart, Object... pathRemainder) {
        this.matchers = Stream.concat(Stream.of(pathStart), Arrays.stream(pathRemainder))
            .map(PathMatcher::of)
            .collect(toList());
    }

    /**
     * A path match that matches everywhere
     * @return matches everything
     */
    public static PathMatch all() {
        return new PathMatch(PathWildCard.ANY_SUBTREE);
    }

    /**
     * Find out whether this path match fits the location
     * @param location the location to check
     * @return <code>true</code> if the path matches
     */
    public boolean matches(Location location) {
        return matchers.get(0).matches(location, matchers.subList(1, matchers.size()));
    }

    @Override
    public String toString() {
        return matchers.toString();
    }
}
