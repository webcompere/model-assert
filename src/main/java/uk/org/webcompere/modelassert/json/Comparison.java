package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.JsonNode;

public interface Comparison {

    Result test(JsonNode json);

    String describe();
}
