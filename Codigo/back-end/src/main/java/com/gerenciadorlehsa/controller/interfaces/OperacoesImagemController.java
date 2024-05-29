package com.gerenciadorlehsa.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface OperacoesImagemController {

    @GetMapping("img/{id}")
    ResponseEntity<?> encontrarImagemPorId (@PathVariable UUID id);
}
