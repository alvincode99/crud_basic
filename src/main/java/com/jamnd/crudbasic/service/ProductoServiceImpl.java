package com.jamnd.crudbasic.service;

import com.jamnd.crudbasic.dto.ProductoRequest;
import com.jamnd.crudbasic.dto.ProductoResponse;
import com.jamnd.crudbasic.entity.Producto;
import com.jamnd.crudbasic.exception.DuplicateSkuException;
import com.jamnd.crudbasic.exception.ResourceNotFoundException;
import com.jamnd.crudbasic.mapper.ProductoMapper;
import com.jamnd.crudbasic.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Implementacion del servicio de productos.
 */
@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public ProductoResponse create(ProductoRequest request) {
        if (productoRepository.existsBySku(request.sku())) {
            throw new DuplicateSkuException("El sku ya existe: " + request.sku());
        }
        Producto entity = productoMapper.toEntity(request);
        Producto saved = productoRepository.save(entity);
        return productoMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponse> findAll(String nombre, Pageable pageable) {
        Page<Producto> page = StringUtils.hasText(nombre)
            ? productoRepository.findByNombreContainingIgnoreCase(nombre, pageable)
            : productoRepository.findAll(pageable);
        return page.map(productoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse findById(Long id) {
        Producto entity = getEntityOrThrow(id);
        return productoMapper.toResponse(entity);
    }

    @Override
    public ProductoResponse update(Long id, ProductoRequest request) {
        Producto entity = getEntityOrThrow(id);
        if (productoRepository.existsBySkuAndIdNot(request.sku(), id)) {
            throw new DuplicateSkuException("El sku ya existe: " + request.sku());
        }
        productoMapper.updateEntity(entity, request);
        Producto updated = productoRepository.save(entity);
        return productoMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Producto entity = getEntityOrThrow(id);
        productoRepository.delete(entity);
    }

    private Producto getEntityOrThrow(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }
}
