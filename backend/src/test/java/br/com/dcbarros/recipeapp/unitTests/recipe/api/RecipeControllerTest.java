package br.com.dcbarros.recipeapp.unitTests.recipe.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import br.com.dcbarros.recipeapp.recipe.api.controller.RecipeController;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

public class RecipeControllerTest {

    @Test
    public void whenPing_thenReturnStatusOk() {
        RecipeController controller = new RecipeController();
        ResponseEntity<Map<String, String>> response = controller.ping();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(Map.of("service", "recipe-app", "status", "ok"), response.getBody());
    }
}
