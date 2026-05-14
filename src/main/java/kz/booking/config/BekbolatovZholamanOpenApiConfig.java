package kz.booking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BekbolatovZholamanOpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Booking System API")
                        .version("0.0.1")
                        .description("Final project: Booking System (Spring Boot, PostgreSQL, JWT)")
                        .license(new License().name("MIT")));
    }
}

