package uk.org.webcompere.modelassert.json;

import java.util.regex.Pattern;

/**
 * Common regular expressions for changeable patterns
 */
public class Patterns {

    /**
     * The standard GUID pattern, supporting upper and lowercase hex
     */
    public static final Pattern GUID_PATTERN =
            Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
}
