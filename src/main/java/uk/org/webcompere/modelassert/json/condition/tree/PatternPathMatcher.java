package uk.org.webcompere.modelassert.json.condition.tree;

import java.util.List;
import java.util.regex.Pattern;

import static uk.org.webcompere.modelassert.json.condition.tree.PathMatcher.matchesTheRest;

public class PatternPathMatcher implements PathMatcher {
    public static final Pattern ANY_FIELD_PATTERN = Pattern.compile(".*");

    private Pattern pattern;

    public PatternPathMatcher(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(Location location, List<PathMatcher> remaining) {
        String first = location.first();
        if (first == null) {
            return false;
        }
        return pattern.matcher(first).matches() && matchesTheRest(location.peelOffFirst(), remaining);
    }

    @Override
    public String toString() {
        return "{" + pattern.pattern() + "}";
    }
}
