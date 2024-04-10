package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.util.UUID;

@Builder
@JsonPropertyOrder({"id", "nome", "tipoItem",
        "quantidade", "valorUnitario", "emprestavel", "imagem"})
public record ItemDTOResponse (
        UUID id,
        @JsonProperty("tipo_item")String tipoItem,
        Integer quantidade,
        @JsonProperty("valor_unitario")Float valorUnitario,
        String nome,
        Boolean emprestavel,
        @JsonProperty("imagem")byte[] bytesImagem
){
}