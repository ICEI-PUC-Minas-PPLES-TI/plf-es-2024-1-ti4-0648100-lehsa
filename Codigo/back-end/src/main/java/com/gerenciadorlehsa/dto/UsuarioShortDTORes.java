package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@Builder
@JsonPropertyOrder({"id", "nome",
        "telefone", "email", "curso"})
public record UsuarioShortDTORes(
        String nome,
        String email,
        String telefone,
        String curso
       ) {
}
