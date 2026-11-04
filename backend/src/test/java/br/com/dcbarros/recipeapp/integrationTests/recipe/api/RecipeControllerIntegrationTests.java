package br.com.dcbarros.recipeapp.integrationTests.recipe.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class RecipeControllerIntegrationTests {

    @Value("${local.server.port}")
    private int port;

    @Test
    void givenAccessToPingEndpoint_thenReturnStatusOk() {
        RestClient client = RestClient.create("http://localhost:" + port);

        ResponseEntity<Map<String, String>> response = client.get()
            .uri("/api/v1/ping")
            .retrieve()
            .toEntity(new ParameterizedTypeReference<Map<String, String>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(
            Map.of("service", "recipe-api", "status", "ok"),
            response.getBody()
        );
    }

}
