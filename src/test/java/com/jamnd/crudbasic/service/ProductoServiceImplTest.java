package com.jamnd.crudbasic.service;

import com.jamnd.crudbasic.dto.ProductoRequest;
import com.jamnd.crudbasic.dto.ProductoResponse;
import com.jamnd.crudbasic.entity.Producto;
import com.jamnd.crudbasic.exception.DuplicateSkuException;
import com.jamnd.crudbasic.exception.ResourceNotFoundException;
import com.jamnd.crudbasic.mapper.ProductoMapper;
import com.jamnd.crudbasic.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    private ProductoServiceImpl productoService;

    @BeforeEach
    void setUp() {
        productoService = new ProductoServiceImpl(productoRepository, new ProductoMapper());
    }

    @Test
    void createDebeRetornarProductoCreado() {
        ProductoRequest request = buildRequest("SKU-UNIT-1", "Producto Unit");
        when(productoRepository.existsBySku("SKU-UNIT-1")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> {
            Producto entity = invocation.getArgument(0);
            entity.setId(1L);
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());
            return entity;
        });

        ProductoResponse response = productoService.create(request);

        assertEquals(1L, response.id());
        assertEquals("SKU-UNIT-1", response.sku());
    }

    @Test
    void createDebeFallarCuandoSkuDuplicado() {
        ProductoRequest request = buildRequest("SKU-REP", "Producto");
        when(productoRepository.existsBySku("SKU-REP")).thenReturn(true);

        assertThrows(DuplicateSkuException.class, () -> productoService.create(request));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void findByIdDebeFallarCuandoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.findById(99L));
    }

    @Test
    void updateDebeActualizarProducto() {
        Producto actual = buildEntity(7L, "SKU-OLD", "Anterior");
        ProductoRequest request = buildRequest("SKU-NEW", "Nuevo");
        when(productoRepository.findById(7L)).thenReturn(Optional.of(actual));
        when(productoRepository.existsBySkuAndIdNot("SKU-NEW", 7L)).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductoResponse response = productoService.update(7L, request);

        assertEquals("SKU-NEW", response.sku());
        assertEquals("Nuevo", response.nombre());
    }

    @Test
    void deleteDebeEliminarProductoExistente() {
        Producto actual = buildEntity(7L, "SKU-DEL", "Eliminar");
        when(productoRepository.findById(7L)).thenReturn(Optional.of(actual));
        doNothing().when(productoRepository).delete(actual);

        productoService.delete(7L);

        verify(productoRepository).delete(actual);
    }

    @Test
    void findAllDebeFiltrarPorNombreCuandoSeEnviaParametro() {
        Producto producto = buildEntity(1L, "SKU-LIST", "Mouse");
        Page<Producto> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 10), 1);
        when(productoRepository.findByNombreContainingIgnoreCase("mou", PageRequest.of(0, 10))).thenReturn(page);

        Page<ProductoResponse> response = productoService.findAll("mou", PageRequest.of(0, 10));

        assertEquals(1, response.getTotalElements());
        assertTrue(response.getContent().getFirst().nombre().contains("Mouse"));
    }

    private ProductoRequest buildRequest(String sku, String nombre) {
        return new ProductoRequest(
            sku,
            nombre,
            "Descripcion",
            new BigDecimal("100.00"),
            5,
            "Categoria",
            true
        );
    }

    private Producto buildEntity(Long id, String sku, String nombre) {
        Producto entity = new Producto();
        entity.setId(id);
        entity.setSku(sku);
        entity.setNombre(nombre);
        entity.setDescripcion("Descripcion");
        entity.setPrecio(new BigDecimal("100.00"));
        entity.setStock(10);
        entity.setCategoria("Categoria");
        entity.setActivo(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
