package com.jamnd.crudbasic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion.
 */
@SpringBootApplication
public class CrudBasicApplication {

    /**
     * Inicia la aplicacion Spring Boot.
     *
     * @param args argumentos de inicio
     */
    public static void main(String[] args) {
        SpringApplication.run(CrudBasicApplication.class, args);
    }
}
