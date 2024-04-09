package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.util.UUID;

@Builder
@JsonPropertyOrder({"id", "nome", "tipoItem",
        "quantidade", "valorUnitario", "emprestavel"})
public record ItemDTO (
    UUID id,
    @JsonProperty("tipo_item")String tipoItem,
    int quantidade,
    @JsonProperty("valor_unitario")float valorUnitario,
    String nome,
    boolean emprestavel
){
}

