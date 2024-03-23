package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SenhaDTO(
        @JsonProperty("senha_original") String senhaOriginal,
        @JsonProperty("senha_atualizada") String senhaAtualizada) {
}
