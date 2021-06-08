package uk.org.webcompere.modelassert.json.condition.tree;

import java.util.List;

import static uk.org.webcompere.modelassert.json.condition.tree.PathMatcher.matchesTheRest;

public class StringPathMatcher implements PathMatcher {
    private String value;

    public StringPathMatcher(String value) {
        this.value = value;
    }

    @Override
    public boolean matches(Location location, List<PathMatcher> remaining) {
        return value.equals(location.first()) && matchesTheRest(location.peelOffFirst(), remaining);
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
