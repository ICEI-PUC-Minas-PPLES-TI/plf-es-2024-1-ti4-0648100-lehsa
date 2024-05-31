package com.gerenciadorlehsa.controller.interfaces;

import com.gerenciadorlehsa.dto.AgendamentoDTORes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProfessorController {
    @PatchMapping("/confirmacao-cadastro")
    ResponseEntity<Map<String, Object>> confirmarCadastro(@RequestParam("id") UUID id);

    @GetMapping("/{id}/agendamentos")
    ResponseEntity<List<AgendamentoDTORes>> listarAgendamentoProfessor(@PathVariable UUID id);
}
