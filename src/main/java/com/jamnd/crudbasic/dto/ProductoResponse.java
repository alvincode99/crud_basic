package com.jamnd.crudbasic.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de salida para exponer datos de un producto.
 *
 * @param id identificador del producto
 * @param sku codigo unico del producto
 * @param nombre nombre del producto
 * @param descripcion descripcion del producto
 * @param precio precio unitario
 * @param stock stock disponible
 * @param categoria categoria del producto
 * @param activo estado del producto
 * @param createdAt fecha de creacion
 * @param updatedAt fecha de actualizacion
 */
@Schema(name = "ProductoResponse", description = "Datos del producto")
public record ProductoResponse(
    @Schema(description = "Id del producto", example = "1")
    Long id,
    @Schema(description = "SKU unico", example = "SKU-1001")
    String sku,
    @Schema(description = "Nombre del producto", example = "Teclado mecanico")
    String nombre,
    @Schema(description = "Descripcion del producto", example = "Teclado para oficina")
    String descripcion,
    @Schema(description = "Precio del producto", example = "129.90")
    BigDecimal precio,
    @Schema(description = "Stock disponible", example = "10")
    Integer stock,
    @Schema(description = "Categoria", example = "Perifericos")
    String categoria,
    @Schema(description = "Estado activo", example = "true")
    Boolean activo,
    @Schema(description = "Fecha de creacion", example = "2026-02-21T12:00:00")
    LocalDateTime createdAt,
    @Schema(description = "Fecha de actualizacion", example = "2026-02-21T12:00:00")
    LocalDateTime updatedAt
) {
}
