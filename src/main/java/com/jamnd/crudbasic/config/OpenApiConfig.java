package com.jamnd.crudbasic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion base de OpenAPI.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Define metadatos de la documentacion API.
     *
     * @return configuracion OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Productos de Tienda")
                .description("CRUD REST con paginacion y filtro por nombre")
                .version("1.0.0")
                .contact(new Contact().name("Equipo Backend").email("backend@example.com"))
                .license(new License().name("Uso academico")));
    }
}
