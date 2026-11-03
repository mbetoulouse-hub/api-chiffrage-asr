package fr.asr.calculator.configuration.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityRequirement bearerAuthRequirement =
                new SecurityRequirement().addList("bearerAuth").addList("basicAuth");

        return new OpenAPI()
                .info(new Info().title("ASR-Calculator API").version("1.0"))
                .components(buildAuth())
                .addSecurityItem(bearerAuthRequirement);
    }

    private Components buildAuth() {
        return new Components()
                .addSecuritySchemes(
                        "bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                .addSecuritySchemes(
                        "basicAuth",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic"));
    }
}
