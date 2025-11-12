package com.example.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PUBLIC_INTERFACE
 * Application entry point for the backend service.
 */
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Click Quest Backend",
                version = "0.1.0",
                description = "REST API for Click Quest scores",
                contact = @Contact(name = "Click Quest")
        )
)
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
