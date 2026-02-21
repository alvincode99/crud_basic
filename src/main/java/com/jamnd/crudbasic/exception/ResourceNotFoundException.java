package com.jamnd.crudbasic.exception;

/**
 * Excepcion para recursos no encontrados.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Crea la excepcion con mensaje.
     *
     * @param message detalle del error
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
