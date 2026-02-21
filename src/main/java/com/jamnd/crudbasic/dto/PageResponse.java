package com.jamnd.crudbasic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DTO generico para responder datos paginados.
 *
 * @param content lista de elementos
 * @param page pagina actual
 * @param size tamano de pagina
 * @param totalElements total de elementos
 * @param totalPages total de paginas
 * @param <T> tipo del contenido
 */
@Schema(name = "PageResponse", description = "Respuesta paginada")
public record PageResponse<T>(
    @Schema(description = "Contenido de la pagina")
    List<T> content,
    @Schema(description = "Numero de pagina actual", example = "0")
    int page,
    @Schema(description = "Tamano de pagina", example = "10")
    int size,
    @Schema(description = "Total de elementos", example = "57")
    long totalElements,
    @Schema(description = "Total de paginas", example = "6")
    int totalPages
) {
    /**
     * Construye una respuesta de pagina a partir de Page.
     *
     * @param pageSource pagina origen
     * @return respuesta lista para API
     * @param <T> tipo del contenido
     */
    public static <T> PageResponse<T> fromPage(Page<T> pageSource) {
        return new PageResponse<>(
            pageSource.getContent(),
            pageSource.getNumber(),
            pageSource.getSize(),
            pageSource.getTotalElements(),
            pageSource.getTotalPages()
        );
    }
}
