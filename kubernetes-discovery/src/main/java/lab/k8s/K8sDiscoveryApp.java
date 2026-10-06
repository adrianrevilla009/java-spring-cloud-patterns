package lab.k8s;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class K8sDiscoveryApp {

    public static void main(String[] args) {
        SpringApplication.run(K8sDiscoveryApp.class, args);
    }

    private final DiscoveryClient discovery;

    K8sDiscoveryApp(DiscoveryClient discovery) {
        this.discovery = discovery;
    }

    /** Services visible in the namespaces this pod's ServiceAccount may read. */
    @GetMapping("/services")
    List<String> services() {
        return discovery.getServices();
    }

    @GetMapping("/services/{name}")
    List<String> instances(@PathVariable String name) {
        return discovery.getInstances(name).stream().map(i -> i.getHost() + ":" + i.getPort()).toList();
    }
}
