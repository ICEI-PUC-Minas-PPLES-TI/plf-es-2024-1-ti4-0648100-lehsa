package com.gerenciadorlehsa.controller.interfaces;

import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Entidade com os métodos padrões do controller
 *
 * @param <I> Classe de entrada do controller
 * @param <O> Classe de saída do controller
 */
public interface OperacoesCRUDControllerImg<I, O> {

    @GetMapping("/{id}")
    ResponseEntity<O> encontrarPorId(@PathVariable UUID id);

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Map<String, Object>> criar(@Valid @RequestPart I obj,
                                              @NotNull @RequestPart MultipartFile img);

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Map<String, Object>> atualizar(@PathVariable UUID id, @Valid @RequestPart I obj, @RequestPart MultipartFile img);

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, Object>> deletar(@PathVariable UUID id);

    @GetMapping
    ResponseEntity<List<O>> listarTodos();
}
