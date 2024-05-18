package com.gerenciadorlehsa.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.UUID;

public interface ProfessorController {
    ResponseEntity<Map<String, Object>> confirmarCadastro(@RequestParam("id") UUID id);
}
