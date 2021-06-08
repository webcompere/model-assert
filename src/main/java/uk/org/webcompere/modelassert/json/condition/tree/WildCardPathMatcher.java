package uk.org.webcompere.modelassert.json.condition.tree;

import uk.org.webcompere.modelassert.json.dsl.nodespecific.tree.PathWildCard;

import java.util.List;

import static uk.org.webcompere.modelassert.json.condition.tree.PatternPathMatcher.ANY_FIELD_PATTERN;

public class WildCardPathMatcher implements PathMatcher {
    private PathWildCard pathWildCard;

    public WildCardPathMatcher(PathWildCard pathWildCard) {
        this.pathWildCard = pathWildCard;
    }

    @Override
    public boolean matches(Location location, List<PathMatcher> remaining) {
        switch (pathWildCard) {
          case ANY_FIELD:
              return new PatternPathMatcher(ANY_FIELD_PATTERN).matches(location, remaining);
          case ANY_SUBTREE:
              Location currentLocation = location;
              while (currentLocation.size() >= remaining.size()) {
                  if (remaining.isEmpty()) {
                      return true;
                  }
                  if (remaining.get(0).matches(currentLocation, remaining.subList(1, remaining.size()))) {
                      return true;
                  }
                  currentLocation = currentLocation.peelOffFirst();
              }
              return false;
          default:
              return false;
        }
    }

    @Override
    public String toString() {
        return pathWildCard.toString();
    }
}
