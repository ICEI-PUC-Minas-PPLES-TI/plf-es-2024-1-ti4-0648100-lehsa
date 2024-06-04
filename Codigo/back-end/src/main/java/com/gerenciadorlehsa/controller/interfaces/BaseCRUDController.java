package com.gerenciadorlehsa.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BaseCRUDController<O> {

    @GetMapping("/{id}")
    ResponseEntity<O> encontrarPorId(@PathVariable UUID id);

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, Object>> deletar(@PathVariable UUID id);

    @GetMapping
    ResponseEntity<List<O>> listarTodos();
}
