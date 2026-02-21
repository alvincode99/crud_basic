package com.jamnd.crudbasic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamnd.crudbasic.dto.ProductoRequest;
import com.jamnd.crudbasic.dto.ProductoResponse;
import com.jamnd.crudbasic.exception.GlobalExceptionHandler;
import com.jamnd.crudbasic.exception.ResourceNotFoundException;
import com.jamnd.crudbasic.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductoController.class)
@Import(GlobalExceptionHandler.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoService productoService;

    @Test
    void createDebeRetornar400CuandoFaltanCampos() throws Exception {
        ProductoRequest request = new ProductoRequest(
            "",
            "",
            "Descripcion",
            new BigDecimal("0.00"),
            -1,
            "Categoria",
            true
        );

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void findAllDebeRetornarPaginaConMetadata() throws Exception {
        ProductoResponse response = buildResponse(1L, "SKU-PAG", "Producto Paginado");
        Page<ProductoResponse> page = new PageImpl<>(
            List.of(response),
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
            1
        );
        when(productoService.findAll("prod", PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))))
            .thenReturn(page);

        mockMvc.perform(get("/api/v1/productos")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "createdAt,desc")
                .param("nombre", "prod"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].sku").value("SKU-PAG"))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void findByIdDebeRetornar404CuandoNoExiste() throws Exception {
        doThrow(new ResourceNotFoundException("Producto no encontrado con id: 99"))
            .when(productoService).findById(any(Long.class));

        mockMvc.perform(get("/api/v1/productos/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Producto no encontrado con id: 99"));
    }

    private ProductoResponse buildResponse(Long id, String sku, String nombre) {
        LocalDateTime now = LocalDateTime.now();
        return new ProductoResponse(
            id,
            sku,
            nombre,
            "Descripcion",
            new BigDecimal("120.00"),
            5,
            "Categoria",
            true,
            now,
            now
        );
    }
}
