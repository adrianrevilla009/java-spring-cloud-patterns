package lab.lb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@RestController
public class LbApp {

    public static void main(String[] args) {
        SpringApplication.run(LbApp.class, args);
    }

    /** "orders" is a logical service id, resolved to instances by the LoadBalancer. */
    @Bean
    @LoadBalanced
    static RestClient.Builder lbBuilder() {
        return RestClient.builder();
    }

    private final RestClient client;
    private final String port;

    LbApp(RestClient.Builder lbBuilder, @Value("${server.port}") String port) {
        this.client = lbBuilder.baseUrl("http://orders").build();
        this.port = port;
    }

    @GetMapping("/whoami")
    String whoami() {
        return port;
    }

    @GetMapping("/call")
    String call() {
        return client.get().uri("/whoami").retrieve().body(String.class);
    }
}
