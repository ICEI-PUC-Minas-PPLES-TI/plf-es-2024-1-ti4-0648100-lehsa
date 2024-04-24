package com.gerenciadorlehsa.controller.interfaces;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Entidade com os métodos padrões do controller
 *
 * @param <I> Classe de entrada do controller
 * @param <O> Classe de saída do controller
 */
public interface OperacoesCRUDController<I, O> {

    @GetMapping("/{id}")
    ResponseEntity<O> encontrarPorId(@PathVariable UUID id);

    @PostMapping
    ResponseEntity<Map<String, Object>> criar(@Valid @RequestBody I obj);

    @PutMapping("/{id}")
    ResponseEntity<Map<String, Object>> atualizar(@PathVariable UUID id, @Valid @RequestBody I obj);

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, Object>> deletar(@PathVariable UUID id);
}
