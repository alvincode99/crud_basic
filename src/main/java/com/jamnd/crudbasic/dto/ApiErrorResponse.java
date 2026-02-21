package com.jamnd.crudbasic.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de error de la API.
 *
 * @param timestamp fecha y hora del error
 * @param status codigo HTTP
 * @param error nombre del error HTTP
 * @param message detalle del error
 * @param path ruta solicitada
 */
@Schema(name = "ApiErrorResponse", description = "Formato de error de la API")
public record ApiErrorResponse(
    @Schema(description = "Fecha del error", example = "2026-02-21T12:00:00")
    LocalDateTime timestamp,
    @Schema(description = "Codigo HTTP", example = "400")
    int status,
    @Schema(description = "Texto HTTP", example = "Bad Request")
    String error,
    @Schema(description = "Mensaje de error", example = "El nombre es obligatorio")
    String message,
    @Schema(description = "Path de la solicitud", example = "/api/v1/productos")
    String path
) {
}
