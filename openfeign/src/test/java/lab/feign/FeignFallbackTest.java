package lab.feign;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "lab.inventory-url=http://localhost:1")
class FeignFallbackTest {

    @Autowired
    TestRestTemplate http;

    @Test
    void fallsBackWhenRemoteIsDown() {
        assertThat(http.getForObject("/orders/book/stock", Integer.class)).isEqualTo(-1);
    }
}
