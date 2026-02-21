package com.jamnd.crudbasic.exception;

/**
 * Excepcion para SKU duplicado.
 */
public class DuplicateSkuException extends RuntimeException {

    /**
     * Crea la excepcion con mensaje.
     *
     * @param message detalle del error
     */
    public DuplicateSkuException(String message) {
        super(message);
    }
}
