package com.jamnd.crudbasic.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamnd.crudbasic.dto.ProductoRequest;
import com.jamnd.crudbasic.entity.Producto;
import com.jamnd.crudbasic.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductoRepository productoRepository;

    @BeforeEach
    void cleanData() {
        productoRepository.deleteAll();
    }

    @Test
    void flujoCrudYListadoConValidacionesDebeFuncionar() throws Exception {
        ProductoRequest createRequest = new ProductoRequest(
            "SKU-INT-1",
            "Mouse Gamer",
            "Mouse para pruebas",
            new BigDecimal("250.00"),
            10,
            "Perifericos",
            true
        );

        MvcResult createResult = mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.sku").value("SKU-INT-1"))
            .andReturn();

        JsonNode createdJson = objectMapper.readTree(createResult.getResponse().getContentAsString());
        long createdId = createdJson.get("id").asLong();

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409));

        mockMvc.perform(get("/api/v1/productos/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404));

        ProductoRequest updateRequest = new ProductoRequest(
            "SKU-INT-1",
            "Mouse Gamer Pro",
            "Mouse actualizado",
            new BigDecimal("300.00"),
            15,
            "Perifericos",
            true
        );

        mockMvc.perform(put("/api/v1/productos/{id}", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Mouse Gamer Pro"))
            .andExpect(jsonPath("$.precio").value(300.00));

        Producto teclado = new Producto();
        teclado.setSku("SKU-INT-2");
        teclado.setNombre("Teclado Office");
        teclado.setDescripcion("Teclado base");
        teclado.setPrecio(new BigDecimal("120.00"));
        teclado.setStock(8);
        teclado.setCategoria("Perifericos");
        teclado.setActivo(true);
        teclado.setCreatedAt(LocalDateTime.now());
        teclado.setUpdatedAt(LocalDateTime.now());
        productoRepository.save(teclado);

        mockMvc.perform(get("/api/v1/productos")
                .param("page", "0")
                .param("size", "1")
                .param("sort", "createdAt,desc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(1))
            .andExpect(jsonPath("$.totalElements").value(2))
            .andExpect(jsonPath("$.totalPages").value(2));

        mockMvc.perform(get("/api/v1/productos")
                .param("nombre", "mou"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.content[0].nombre").value("Mouse Gamer Pro"));

        ProductoRequest invalidRequest = new ProductoRequest(
            "",
            "",
            "Invalido",
            new BigDecimal("0"),
            -1,
            "Test",
            true
        );

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400));

        mockMvc.perform(delete("/api/v1/productos/{id}", createdId))
            .andExpect(status().isNoContent());

        assertThat(productoRepository.findById(createdId)).isEmpty();
    }
}
