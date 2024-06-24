package com.gerenciadorlehsa.dto;

import com.gerenciadorlehsa.entity.enums.UF;

import java.util.UUID;

public record EnderecoDTO(
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
