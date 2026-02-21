package com.jamnd.crudbasic.repository;

import com.jamnd.crudbasic.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para operaciones de persistencia de productos.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca productos por nombre con contains e ignore case.
     *
     * @param nombre texto a buscar
     * @param pageable datos de paginacion
     * @return pagina de productos filtrados
     */
    Page<Producto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    /**
     * Valida si existe un SKU.
     *
     * @param sku sku a validar
     * @return true si existe
     */
    boolean existsBySku(String sku);

    /**
     * Valida si existe un SKU en un registro distinto.
     *
     * @param sku sku a validar
     * @param id id del registro actual
     * @return true si existe en otro registro
     */
    boolean existsBySkuAndIdNot(String sku, Long id);
}
