package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static com.fasterxml.jackson.dataformat.yaml.YAMLParser.Feature.PARSE_BOOLEAN_LIKE_WORDS_AS_STRINGS;
import static uk.org.webcompere.modelassert.json.JsonProviders.*;

// common overrides of object mapper
abstract class OverrideObjectMapper {
    @BeforeAll
    static void beforeAll() {
        overrideObjectMapper(defaultObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
        overrideYamlObjectMapper(new ObjectMapper(
            new YAMLFactory()
                .configure(PARSE_BOOLEAN_LIKE_WORDS_AS_STRINGS, true)));
    }

    @AfterAll
    static void afterAll() {
        clearObjectMapperOverride();
        clearYamlObjectMapperOverride();
    }
}
