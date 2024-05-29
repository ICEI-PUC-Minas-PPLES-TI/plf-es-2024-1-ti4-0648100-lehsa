package com.gerenciadorlehsa.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AgendamentoController extends TransacaoController{

    @PatchMapping("/tecnico/{id}/{email}")
    ResponseEntity<Map<String, Object>> atualizarTecnico (@PathVariable UUID id,
                                                                 @PathVariable String email);
    @PatchMapping("/professor-confirma")
    ResponseEntity<Map<String, Object>> confirmarAgendamento(@RequestParam("id") UUID id);


    @GetMapping("/datasOcupadas")
    ResponseEntity<List<String[]>> listarDatasOcupadas ();


}
