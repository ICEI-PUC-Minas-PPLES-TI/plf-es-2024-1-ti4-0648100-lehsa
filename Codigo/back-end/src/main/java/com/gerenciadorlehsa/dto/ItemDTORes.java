package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import java.util.UUID;

@Builder
@JsonPropertyOrder({"id", "nome", "tipoItem"})
public record ItemDTORes(
        UUID id,
        @JsonProperty("tipo_item")String tipoItem,
        String nome
){
}