package lab.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GatewayApp {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }

    /** Stand-in for the orders service so the lab needs no second process. */
    @GetMapping("/internal/orders")
    String orders() {
        return "[{\"id\":1,\"sku\":\"book\"}]";
    }

    @Bean
    RouteLocator routes(RouteLocatorBuilder b, @Value("${lab.orders-uri}") String ordersUri) {
        return b.routes()
                .route("orders", r -> r.path("/api/orders")
                        .filters(f -> f.rewritePath("/api/orders", "/internal/orders")
                                .addRequestHeader("X-Lab-Caller", "gateway")
                                .addResponseHeader("X-Lab-Route", "orders"))
                        .uri(ordersUri))
                .build();
    }
}
