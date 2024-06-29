package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gerenciadorlehsa.entity.enums.StatusTransacao;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
@JsonPropertyOrder({"id","dataHoraInicio","dataHoraFim", "solicitante", "itens", "endereco", "observacaoSolicitacao"})
public record EmprestimoDTORes(
        UUID id,
        UsuarioShortDTORes solicitante,
        List<ItemDTORes> itens,
        EnderecoDTORes endereco,
        String dataHoraInicio,
        String dataHoraFim,
        String observacaoSolicitacao,
        StatusTransacao statusTransacao
) {
}
