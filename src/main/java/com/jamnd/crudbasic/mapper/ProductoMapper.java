package com.jamnd.crudbasic.mapper;

import com.jamnd.crudbasic.dto.ProductoRequest;
import com.jamnd.crudbasic.dto.ProductoResponse;
import com.jamnd.crudbasic.entity.Producto;
import org.springframework.stereotype.Component;

/**
 * Mapper para transformar entidad y DTOs de producto.
 */
@Component
public class ProductoMapper {

    /**
     * Convierte un request a entidad.
     *
     * @param request datos de entrada
     * @return entidad de producto
     */
    public Producto toEntity(ProductoRequest request) {
        Producto entity = new Producto();
        entity.setSku(request.sku());
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setPrecio(request.precio());
        entity.setStock(request.stock());
        entity.setCategoria(request.categoria());
        entity.setActivo(request.activo() == null ? Boolean.TRUE : request.activo());
        return entity;
    }

    /**
     * Actualiza una entidad existente con datos del request.
     *
     * @param entity entidad a actualizar
     * @param request nuevos datos
     */
    public void updateEntity(Producto entity, ProductoRequest request) {
        entity.setSku(request.sku());
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setPrecio(request.precio());
        entity.setStock(request.stock());
        entity.setCategoria(request.categoria());
        entity.setActivo(request.activo() == null ? Boolean.TRUE : request.activo());
    }

    /**
     * Convierte entidad a DTO de respuesta.
     *
     * @param entity entidad origen
     * @return dto de salida
     */
    public ProductoResponse toResponse(Producto entity) {
        return new ProductoResponse(
            entity.getId(),
            entity.getSku(),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getPrecio(),
            entity.getStock(),
            entity.getCategoria(),
            entity.getActivo(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
