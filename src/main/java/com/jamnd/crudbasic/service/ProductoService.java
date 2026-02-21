package com.jamnd.crudbasic.service;

import com.jamnd.crudbasic.dto.ProductoRequest;
import com.jamnd.crudbasic.dto.ProductoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contrato de servicio para casos de uso de producto.
 */
public interface ProductoService {

    /**
     * Crea un nuevo producto.
     *
     * @param request datos del producto
     * @return producto creado
     */
    ProductoResponse create(ProductoRequest request);

    /**
     * Lista productos con paginacion y filtro por nombre.
     *
     * @param nombre filtro opcional por nombre
     * @param pageable datos de paginacion
     * @return pagina de productos
     */
    Page<ProductoResponse> findAll(String nombre, Pageable pageable);

    /**
     * Obtiene un producto por id.
     *
     * @param id id del producto
     * @return producto encontrado
     */
    ProductoResponse findById(Long id);

    /**
     * Actualiza un producto por id.
     *
     * @param id id a actualizar
     * @param request nuevos datos
     * @return producto actualizado
     */
    ProductoResponse update(Long id, ProductoRequest request);

    /**
     * Elimina un producto por id.
     *
     * @param id id a eliminar
     */
    void delete(Long id);
}
