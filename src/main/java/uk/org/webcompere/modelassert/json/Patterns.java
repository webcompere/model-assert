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

    /**
     * ISO 8601 plain date format - e.g. 2021-06-09
     */
    public static final Pattern ISO_8601_DATE =
        Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    /**
     * ISO 8601 date/time format - e.g. 2021-06-09T20:00:09Z
     */
    public static final Pattern ISO_8601_DATE_TIME =
        Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z");

    /**
     * ISO 8601 zoned date time format - e.g. 2021-06-09T20:00:09+00:00
     */
    public static final Pattern ISO_8601_ZONED_DATE_TIME =
        Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[-+]\\d{2}:\\d{2}");

    /**
     * Matches with any of the above formats
     */
    public static final Pattern ISO_8601_DATE_ANY =
        Pattern.compile("\\d{4}-\\d{2}-\\d{2}(T\\d{2}:\\d{2}:\\d{2}([-+]\\d{2}:\\d{2}|Z))?");

    /**
     * Match any integer, positive or negative
     */
    public static final Pattern ANY_INTEGER = Pattern.compile("-?\\d+");
}
