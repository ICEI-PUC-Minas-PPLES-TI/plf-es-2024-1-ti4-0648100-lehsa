package com.gerenciadorlehsa.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.UUID;

public interface AgendamentoController {

    @PatchMapping("/{id}/{status}")
    ResponseEntity<Map<String, Object>> atualizarStatus (@PathVariable UUID id,
                                                                @PathVariable String status);

    @PatchMapping("/tecnico/{id}/{email}")
    ResponseEntity<Map<String, Object>> atualizarTecnico (@PathVariable UUID id,
                                                                 @PathVariable String email);

    ResponseEntity<Map<String, Object>> confirmarAgendamento(@RequestParam("id") UUID id);

}
