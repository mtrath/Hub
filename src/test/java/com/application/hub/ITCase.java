package com.application.hub;

import com.application.hub.clients.HubClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ITCase {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    @LocalServerPort
    private int localServerPort;

    private HubClient hubClient;

    @BeforeEach
    void setUp() {
        hubClient = Feign.builder().target(HubClient.class, "http://localhost:%s".formatted(localServerPort));
    }

    @Test
    void testGetMembers() throws JsonProcessingException {
        // given
        final JsonNode expected = objectMapper.readTree(("[" +
                "{\"id\":1,\"reposUrl\":\"http://localhost:%1$s/users/myuser/repos\"}," +
                "{\"id\":2,\"reposUrl\":\"http://localhost:%1$s/users/anotheruser/repos/users/anotheruser/repos\"}" +
                "]").formatted(localServerPort));

        // when
        final String members = hubClient.getMembers("codecentric");

        // then
        final JsonNode tree = objectMapper.readTree(members);
        assertThat(tree).isEqualTo(expected);
    }

    @Test
    void testGetRepositories() throws JsonProcessingException {
        // given
        final JsonNode expected = objectMapper.readTree("[{\"id\":1,\"name\":\"Hello&World\",\"languagesUrl\":\"http://localhost:%s/repos/myuser/Hello%%26World/languages\"}]".formatted(localServerPort));

        // when
        final String members = hubClient.getRepositories("myuser");

        // then
        final JsonNode tree = objectMapper.readTree(members);
        assertThat(tree).isEqualTo(expected);
    }

    @Test
    void testGetLanguages() throws JsonProcessingException {
        // given
        final JsonNode expected = objectMapper.readTree("{\"Python\":500,\"C\":100}");

        // when
        final String members = hubClient.getLanguages("myuser", "Hello&World");

        // then
        final JsonNode tree = objectMapper.readTree(members);
        assertThat(tree).isEqualTo(expected);
    }
}
