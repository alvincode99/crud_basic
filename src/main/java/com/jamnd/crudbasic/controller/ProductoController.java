package com.jamnd.crudbasic.controller;

import com.jamnd.crudbasic.dto.ApiErrorResponse;
import com.jamnd.crudbasic.dto.PageResponse;
import com.jamnd.crudbasic.dto.ProductoRequest;
import com.jamnd.crudbasic.dto.ProductoResponse;
import com.jamnd.crudbasic.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Controlador REST para CRUD de productos.
 */
@Validated
@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "API para administrar productos de tienda")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Crea un producto.
     *
     * @param request datos del producto
     * @return producto creado
     */
    @Operation(summary = "Crear producto", description = "Crea un producto nuevo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado",
            content = @Content(schema = @Schema(implementation = ProductoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Error de validacion",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "SKU duplicado",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ProductoResponse> create(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse response = productoService.create(request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Lista productos con paginacion y filtro opcional.
     *
     * @param page numero de pagina
     * @param size tamano de pagina
     * @param sort orden como campo,direccion
     * @param nombre filtro opcional por nombre
     * @return respuesta paginada
     */
    @Operation(
        summary = "Listar productos",
        description = "Lista productos con paginacion, orden y filtro opcional por nombre"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido"),
        @ApiResponse(responseCode = "400", description = "Parametros invalidos",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponse<ProductoResponse>> findAll(
        @Parameter(description = "Pagina actual, inicia en 0")
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "page debe ser mayor o igual a 0") int page,
        @Parameter(description = "Tamano de pagina")
        @RequestParam(defaultValue = "10") @Min(value = 1, message = "size debe ser mayor o igual a 1") int size,
        @Parameter(description = "Orden en formato campo,direccion. Ejemplo: createdAt,desc")
        @RequestParam(defaultValue = "createdAt,desc") String sort,
        @Parameter(description = "Filtro opcional por nombre con contains ignore case")
        @RequestParam(required = false) String nombre
    ) {
        Pageable pageable = PageRequest.of(page, size, buildSort(sort));
        Page<ProductoResponse> result = productoService.findAll(nombre, pageable);
        return ResponseEntity.ok(PageResponse.fromPage(result));
    }

    /**
     * Obtiene un producto por id.
     *
     * @param id id del producto
     * @return producto encontrado
     */
    @Operation(summary = "Obtener producto", description = "Obtiene un producto por su id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    /**
     * Actualiza un producto por id.
     *
     * @param id id del producto
     * @param request datos nuevos
     * @return producto actualizado
     */
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado"),
        @ApiResponse(responseCode = "400", description = "Error de validacion",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "SKU duplicado",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> update(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.update(id, request));
    }

    /**
     * Elimina un producto por id.
     *
     * @param id id del producto
     * @return respuesta sin contenido
     */
    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Sort buildSort(String sortParam) {
        String[] parts = sortParam.split(",");
        String property = parts.length > 0 && !parts[0].isBlank() ? parts[0].trim() : "createdAt";
        Sort.Direction direction = Sort.Direction.DESC;
        if (parts.length > 1) {
            direction = Sort.Direction.fromOptionalString(parts[1].trim()).orElse(Sort.Direction.DESC);
        }
        return Sort.by(direction, property);
    }
}
