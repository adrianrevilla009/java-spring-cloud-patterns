package lab.feign;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"server.port=18082", "lab.inventory-url=http://localhost:18082"})
class FeignTest {

    @Autowired
    TestRestTemplate http;

    @Test
    void callsRemoteService() {
        assertThat(http.getForObject("/orders/book/stock", Integer.class)).isEqualTo(7);
    }
}
