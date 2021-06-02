package uk.org.webcompere.modelassert.json;

import java.util.regex.Pattern;

public class Patterns {
    public static final Pattern GUID_PATTERN =
            Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
}
