package com.manish.randomgengames;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiTest {

    // Sends test HTTP requests to the API without starting a real server.
    @Autowired
    private MockMvc mockMvc;

    // Verifies that the home page redirects to Swagger UI.
    @Test
    void homeRedirectsToSwaggerUi() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/swagger-ui.html"));
    }

    // Verifies that the OpenAPI document lists and describes every game endpoint.
    @Test
    void openApiDocumentContainsGameEndpoints() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths", hasKey("/api/number-game/round")))
                .andExpect(jsonPath("$.paths", hasKey("/api/number-game/guess")))
                .andExpect(jsonPath("$.paths", hasKey("/api/name-game/round")))
                .andExpect(jsonPath("$.paths", hasKey("/api/name-game/guess")))
                .andExpect(jsonPath("$.paths['/api/number-game/round'].post.description").isNotEmpty())
                .andExpect(jsonPath("$.paths['/api/number-game/guess'].post.description").isNotEmpty())
                .andExpect(jsonPath("$.paths['/api/name-game/round'].post.description").isNotEmpty())
                .andExpect(jsonPath("$.paths['/api/name-game/guess'].post.description").isNotEmpty());
    }
}
