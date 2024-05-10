package com.gerenciadorlehsa.controller.interfaces;


import com.gerenciadorlehsa.dto.SenhaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * Entidade com os métodos padrões do UsuarioController
 */
public interface UsuarioController {
    @PutMapping("/atualizar-senha/{id}")
    ResponseEntity<Map<String, Object>> atualizarSenha(@PathVariable UUID id, @RequestBody SenhaDTO senhaDTO);

    @PutMapping("/perfil/{id}")
    ResponseEntity<Map<String, Object>> atualizarPerfil(
            @PathVariable("id") UUID id,
            @RequestParam("codigoPerfil") Integer codigoPerfil);

    @GetMapping("/verificar-token")
    ResponseEntity<?> verificarToken(@RequestParam("token") String token);
}
