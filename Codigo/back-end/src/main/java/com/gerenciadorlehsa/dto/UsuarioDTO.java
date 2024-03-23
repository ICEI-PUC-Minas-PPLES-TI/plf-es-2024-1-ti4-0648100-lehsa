package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record UsuarioDTO(
        UUID id,
        @JsonProperty("perfil_usuario") Integer perfilUsuario,
        String fullName,
        String cpf,
        String email,
        String telefone,
        List<Link> links) {
}
