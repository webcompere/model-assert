package uk.org.webcompere.modelassert.json.condition.tree;

import uk.org.webcompere.modelassert.json.dsl.nodespecific.tree.PathWildCard;

import java.util.List;
import java.util.regex.Pattern;

public interface PathMatcher {

    /**
     * Does this path matcher prevent a match, or do any of its successors prevent a match
     * @param location the location
     * @param remaining the remaining {@link PathMatcher}s
     * @return <code>true</code> if there's a match
     */
    boolean matches(Location location, List<PathMatcher> remaining);

    /**
     * Factory method - convert an object into its path matcher
     * @param value the value to convert
     * @return a path matcher or {@link IllegalArgumentException} if not known
     */
    static PathMatcher of(Object value) {
        if (value instanceof String) {
            return new StringPathMatcher((String) value);
        }

        if (value instanceof PathWildCard) {
            return new WildCardPathMatcher((PathWildCard) value);
        }

        if (value instanceof Pattern) {
            return new PatternPathMatcher((Pattern) value);
        }

        throw new IllegalArgumentException("Unexpected path part: " + value +
            ". Expecting String, Pattern or PathWildCard");
    }

    /**
     * Work out whether the rest of the location meets the rest of the remaining matchers
     * @param location the location so far
     * @param remaining the remaining matchers
     * @return true if matches the remainder
     */
    static boolean matchesTheRest(Location location, List<PathMatcher> remaining) {
        if (remaining.isEmpty() && location.isEmpty()) {
            return true;
        }
        if (!location.isEmpty() && remaining.isEmpty()) {
            return false;
        }
        return remaining.get(0)
            .matches(location, remaining.subList(1, remaining.size()));
    }
}
