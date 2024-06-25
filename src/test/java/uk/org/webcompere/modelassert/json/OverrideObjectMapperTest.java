package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;

import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertYaml;

class OverrideObjectMapperTest extends OverrideObjectMapper {
    public static class DateHolder {
        public LocalDateTime date;
    }

    @Test
    void objectWithDateInItCanBeAsserted() {
        DateHolder object = new DateHolder();
        object.date = LocalDateTime.of(2022, 5, 1, 23, 0);

        assertJson(object).isEqualTo("{\"date\":\"2022-05-01T23:00:00\"}");
    }

    @Test
    void yamlWithDateInItCanBeAsserted() {
        assertYaml("item: yes").isEqualToYaml("item: 'yes'");
    }
}
