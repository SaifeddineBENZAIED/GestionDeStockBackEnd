package com.benzaied.gestiondestock.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                contact = @Contact(
                        name = "Bnz Saif",
                        email = "saifeddine.benzaied@insat.ucat.tn"
                ),
                description = "OpenApi documentation for the project",
                title = "OpenApi Doc",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "BearerAuth"
                )
        }
)
@SecurityScheme(
        name = "BearerAuth",
        description = "Jwt auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)

public class OpenApiConfig {

    /*@Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Project Doc")
                        .description("Description of project")
                        .version("1.0"))
                ;
    }*/
}
