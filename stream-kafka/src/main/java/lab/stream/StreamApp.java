package lab.stream;

import java.util.function.Function;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StreamApp {

    public static void main(String[] args) {
        SpringApplication.run(StreamApp.class, args);
    }

    /** orders-in -> processOrder -> orders-out; the binder (Kafka) maps these to topics. */
    @Bean
    Function<String, String> processOrder() {
        return sku -> "CONFIRMED:" + sku.toUpperCase();
    }
}
