package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gerenciadorlehsa.entity.enums.UF;
import lombok.Builder;

import java.util.UUID;

@Builder
@JsonPropertyOrder({"id","cep","uf", "cidade", "bairro", "rua", "numero", "complemento"})
public record EnderecoDTORes(
        UUID id,
        String rua,
        String bairro,
        UF uf,
        String cidade,
        Integer numero,
        String complemento,
        String cep
) {
}
