package lab.eureka;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

class EurekaTest {

    static final String INSTANCE = """
        {"instance":{"instanceId":"demo-1","hostName":"localhost","app":"DEMO","ipAddr":"127.0.0.1",
         "status":"UP","port":{"$":9000,"@enabled":"true"},
         "dataCenterInfo":{"@class":"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo","name":"MyOwn"}}}
        """;

    @Test
    void instanceRegisteredOverRestAppearsInRegistry() {
        try (var server = SpringApplication.run(EurekaApp.class, "--server.port=18761")) {
            RestClient http = RestClient.create("http://localhost:18761");
            http.post().uri("/eureka/apps/DEMO").contentType(MediaType.APPLICATION_JSON)
                    .body(INSTANCE).retrieve().toBodilessEntity();
            String apps = http.get().uri("/eureka/apps/DEMO").accept(MediaType.APPLICATION_JSON)
                    .retrieve().body(String.class);
            assertThat(apps).contains("\"instanceId\":\"demo-1\"").contains("\"status\":\"UP\"");
        }
    }
}
