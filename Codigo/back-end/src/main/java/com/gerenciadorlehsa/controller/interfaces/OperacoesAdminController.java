package com.gerenciadorlehsa.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OperacoesAdminController<I, O> {

    @GetMapping
    ResponseEntity<List<O>> listarTodos();

    @PutMapping("/perfil/{id}")
    ResponseEntity<Map<String, Object>> atualizarPerfil(@PathVariable("id") UUID id, @RequestParam("codigoPerfil") Integer codigoPerfil);

}
