package lab.lb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestClient;

class LbTest {

    static final String[] INSTANCES = {
        "--spring.cloud.discovery.client.simple.instances.orders[0].uri=http://localhost:18091",
        "--spring.cloud.discovery.client.simple.instances.orders[1].uri=http://localhost:18092"};

    static ConfigurableApplicationContext start(int port) {
        return SpringApplication.run(LbApp.class, new String[] {
            "--server.port=" + port, INSTANCES[0], INSTANCES[1]});
    }

    @Test
    void roundRobinsAcrossBothInstances() {
        try (var a = start(18091); var b = start(18092)) {
            RestClient http = RestClient.create("http://localhost:18091");
            Set<String> seen = new HashSet<>();
            for (int i = 0; i < 4; i++) {
                seen.add(http.get().uri("/call").retrieve().body(String.class));
            }
            assertThat(seen).containsExactlyInAnyOrder("18091", "18092");
        }
    }
}
