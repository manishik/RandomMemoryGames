package com.manish.randomgengames.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Memory Games API",
                version = "v1",
                description = "Create random-number or random-name memory rounds and submit guesses"
        )
)
public class OpenApiConfig {
}
