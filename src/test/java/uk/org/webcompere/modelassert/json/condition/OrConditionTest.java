package uk.org.webcompere.modelassert.json.condition;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class OrConditionTest {

    @Mock
    private Condition condition1;

    @Mock
    private Condition condition2;

    @Mock
    private JsonNode mockNode;

    private OrCondition orCondition;

    @BeforeEach
    void beforeEach() {
        orCondition = new OrCondition(condition1, condition2);
    }

    @Test
    void whenFirstConditionPassesThenOrPassesAndShortCircuits() {
        given(condition1.test(any()))
            .willReturn(new Result("a", "b", true));

        assertThat(orCondition.test(mockNode).isPassed()).isTrue();
        then(condition2)
            .should(never())
            .test(any());
    }

    @Test
    void whenFirstConditionFailsAndSecondFailsThenAOrFails() {
        given(condition1.test(any()))
            .willReturn(new Result("a", "b", false));

        given(condition2.test(any()))
            .willReturn(new Result("a", "b", false));

        assertThat(orCondition.test(mockNode).isPassed()).isFalse();
    }

    @Test
    void whenFirstFailsAndSecondPassesThenPasses() {
        given(condition1.test(any()))
            .willReturn(new Result("a", "b", false));

        given(condition2.test(any()))
            .willReturn(new Result("a", "b", true));

        assertThat(orCondition.test(mockNode).isPassed()).isTrue();
    }
}
