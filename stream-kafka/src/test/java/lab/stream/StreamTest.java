package lab.stream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.messaging.support.GenericMessage;

class StreamTest {

    @Test
    void processesOrderFromInputToOutputTopic() {
        try (var ctx = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(StreamApp.class))
                .web(WebApplicationType.NONE)
                .run("--spring.cloud.function.definition=processOrder")) {
            ctx.getBean(InputDestination.class).send(new GenericMessage<>("book".getBytes()), "orders");
            var out = ctx.getBean(OutputDestination.class).receive(2000, "orders-confirmed");
            assertThat(new String(out.getPayload())).isEqualTo("CONFIRMED:BOOK");
        }
    }
}
