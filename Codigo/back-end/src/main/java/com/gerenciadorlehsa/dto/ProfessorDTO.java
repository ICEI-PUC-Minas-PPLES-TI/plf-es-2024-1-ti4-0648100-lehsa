package com.gerenciadorlehsa.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.util.UUID;

@Builder
@JsonPropertyOrder({"id", "nome", "email", "matricula", "laboratorio","campus", "lotacao", "areaAtuacao",
        "confirmaCadastro", "dataHoraCadastro"})
public record ProfessorDTO(
        UUID id,
        String nome,
        String email,
        String matricula,

        String laboratorio,

        String campus,

        String lotacao,

        @JsonProperty("data_hora_cadastro") String dataHoraCadastro,

        @JsonProperty("area_atuacao") String areaAtuacao,
        @JsonProperty("confirma_cadastro") String confirmaCadastro
) {
}
