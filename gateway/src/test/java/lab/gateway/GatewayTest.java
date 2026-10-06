package lab.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"server.port=18080", "lab.orders-uri=http://localhost:18080"})
class GatewayTest {

    @Test
    void routeRewritesPathAndAddsHeader() {
        WebTestClient c = WebTestClient.bindToServer().baseUrl("http://localhost:18080").build();
        c.get().uri("/api/orders").exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("X-Lab-Route", "orders")
                .expectBody(String.class).value(b -> assertThat(b).contains("book"));
    }
}
