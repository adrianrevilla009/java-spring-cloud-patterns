package lab.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableFeignClients
@RestController
public class FeignApp {

    public static void main(String[] args) {
        SpringApplication.run(FeignApp.class, args);
    }

    /** Declarative HTTP client; the fallback runs when the call fails or the circuit is open. */
    @FeignClient(name = "inventory", url = "${lab.inventory-url}", fallback = InventoryFallback.class)
    interface InventoryClient {
        @GetMapping("/stock/{sku}")
        int stock(@PathVariable("sku") String sku);
    }

    @Component
    static class InventoryFallback implements InventoryClient {
        @Override
        public int stock(String sku) {
            return -1; // unknown: callers treat -1 as "do not promise delivery"
        }
    }

    /** Stand-in inventory service. */
    @GetMapping("/stock/{sku}")
    int stockEndpoint(@PathVariable String sku) {
        return 7;
    }

    private final InventoryClient client;

    FeignApp(InventoryClient client) {
        this.client = client;
    }

    @GetMapping("/orders/{sku}/stock")
    int viaClient(@PathVariable String sku) {
        return client.stock(sku);
    }
}
