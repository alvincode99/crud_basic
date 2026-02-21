package com.jamnd.crudbasic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO de entrada para crear o actualizar un producto.
 *
 * @param sku codigo unico del producto
 * @param nombre nombre del producto
 * @param descripcion descripcion del producto
 * @param precio precio unitario
 * @param stock stock disponible
 * @param categoria categoria del producto
 * @param activo indica si el producto esta activo
 */
@Schema(name = "ProductoRequest", description = "Datos para crear o actualizar un producto")
public record ProductoRequest(
    @Schema(description = "SKU unico del producto", example = "SKU-1001")
    @NotBlank(message = "El sku es obligatorio")
    String sku,

    @Schema(description = "Nombre del producto", example = "Teclado mecanico")
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,

    @Schema(description = "Descripcion del producto", example = "Teclado para oficina")
    String descripcion,

    @Schema(description = "Precio del producto", example = "129.90")
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a cero")
    BigDecimal precio,

    @Schema(description = "Stock disponible", example = "10")
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    Integer stock,

    @Schema(description = "Categoria del producto", example = "Perifericos")
    String categoria,

    @Schema(description = "Estado del producto", example = "true")
    Boolean activo
) {
}
