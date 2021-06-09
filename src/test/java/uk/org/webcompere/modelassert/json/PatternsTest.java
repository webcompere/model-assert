package uk.org.webcompere.modelassert.json;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.webcompere.modelassert.json.Patterns.*;

class PatternsTest {

    @Test
    void theGuidPatternMatchesGuid() {
        assertThat("611901a1-7b59-4ab9-b0d9-7b7bb02afd0b")
            .matches(GUID_PATTERN);
    }

    @Test
    void isoDateOnly() {
        assertThat("2021-06-09").matches(ISO_8601_DATE);
    }

    @Test
    void isoDateTimeOnly() {
        assertThat("2021-06-09T20:00:09Z").matches(ISO_8601_DATE_TIME);
    }

    @Test
    void isoZonedDateTimeOnly() {
        assertThat("2021-06-09T20:00:09+00:00").matches(ISO_8601_ZONED_DATE_TIME);
    }

    @Test
    void isoDateAny() {
        assertThat("2021-06-09").matches(ISO_8601_DATE_ANY);
        assertThat("2021-06-09T20:00:09Z").matches(ISO_8601_DATE_ANY);
        assertThat("2021-06-09T20:00:09+00:00").matches(ISO_8601_DATE_ANY);
    }

    @Test
    void anyIntegerPositive() {
        assertThat("1234").matches(ANY_INTEGER);
    }

    @Test
    void anyIntegerNegative() {
        assertThat("-1234").matches(ANY_INTEGER);
    }
}
