package lab.fn;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.function.context.FunctionCatalog;

@SpringBootTest
class FunctionTest {

    @Autowired
    FunctionCatalog catalog;

    @Test
    void functionIsLookedUpByNameFromCatalog() {
        Function<String, String> f = catalog.lookup(Function.class, "priceOrder");
        assertThat(f.apply("book")).isEqualTo("book:40");
    }
}
