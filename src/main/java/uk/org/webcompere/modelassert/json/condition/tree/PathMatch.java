package uk.org.webcompere.modelassert.json.condition.tree;

import uk.org.webcompere.modelassert.json.PathWildCard;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Describes how a path may be matched
 */
public class PathMatch {
    private static final String JSON_POINTER_DELIMITER = "/";

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

    private PathMatch(String[] fixedPath) {
        this.matchers = Arrays.stream(fixedPath)
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
     * Convert from JSON Pointer to path match
     * @param jsonPointer the JSON pointer express
     * @return a new {@link PathMatch}
     */
    public static PathMatch ofJsonPointer(String jsonPointer) {
        if (!jsonPointer.startsWith(JSON_POINTER_DELIMITER)) {
            throw new IllegalArgumentException("Invalid JSON Pointer, must start with " + JSON_POINTER_DELIMITER);
        }

        String[] parts = jsonPointer.split(JSON_POINTER_DELIMITER);
        String[] nonBlankParts = Arrays.copyOfRange(parts, 1, parts.length);
        if (Arrays.stream(nonBlankParts).anyMatch(part -> !part.trim().equals(part) || part.isEmpty())) {
            throw new IllegalArgumentException("Invalid spacing or blanks in " + jsonPointer);
        }

        return new PathMatch(nonBlankParts);
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
