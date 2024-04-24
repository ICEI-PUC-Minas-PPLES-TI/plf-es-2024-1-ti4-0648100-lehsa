package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@JsonPropertyOrder({"id", "nome", "cpf",
        "telefone", "email", "perfilUsuario", "nota","curso", "tipoCurso", "statusCurso"})
public record UsuarioDTO(
        UUID id,
        @JsonProperty("perfil_usuario") Integer perfilUsuario,
        String nome,
        String cpf,
        String email,
        String telefone,
        String curso,
        @JsonProperty("tipo_curso") String tipoCurso,
        @JsonProperty("status_curso") String statusCurso,
        Double nota) {
}
