package lab.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.eclipse.jgit.api.Git;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfigServerTest {

    static Path repo;

    @BeforeAll
    static void createRepo() throws Exception {
        repo = Files.createTempDirectory("orders-config");
        try (Git git = Git.init().setDirectory(repo.toFile()).setInitialBranch("main").call()) {
            commit(git, "orders.maxItems=10", "v1");
        }
    }

    static void commit(Git git, String content, String msg) throws Exception {
        Files.writeString(repo.resolve("orders.properties"), content + "\n");
        git.add().addFilepattern(".").call();
        git.commit().setMessage(msg).setSign(false).setAuthor("lab", "lab@example.com").call();
    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.cloud.config.server.git.uri", () -> "file://" + repo);
    }

    @Autowired
    TestRestTemplate http;

    @Test
    void servesFromGitAndSeesNewCommits() throws Exception {
        assertThat(http.getForObject("/orders/default", String.class)).contains("\"orders.maxItems\":\"10\"");
        try (Git git = Git.open(repo.toFile())) {
            commit(git, "orders.maxItems=25", "v2");
        }
        assertThat(http.getForObject("/orders/default", String.class)).contains("\"orders.maxItems\":\"25\"");
    }
}
