package com.taskmanager.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Trackio API",
                version = "v1",
                description = "API REST para gerenciamento de tarefas",
                contact = @Contact(name = "Trackio")
        )
)
public class OpenApiConfig {
}
