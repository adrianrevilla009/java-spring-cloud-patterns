package lab.fn;

import java.util.function.Function;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Packaging-neutral business logic: the same bean can be exposed over HTTP, Stream,
 * or a serverless adapter (AWS Lambda, Azure, GCP) by swapping the dependency.
 */
@SpringBootApplication
public class FunctionApp {

    public static void main(String[] args) {
        SpringApplication.run(FunctionApp.class, args);
    }

    @Bean
    Function<String, String> priceOrder() {
        return sku -> sku + ":" + (sku.length() * 10);
    }
}
