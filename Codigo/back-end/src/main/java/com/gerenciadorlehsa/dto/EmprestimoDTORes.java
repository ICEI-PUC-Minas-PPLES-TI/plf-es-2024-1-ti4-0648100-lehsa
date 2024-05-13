package com.gerenciadorlehsa.dto;

import com.gerenciadorlehsa.entity.enums.StatusTransacaoItem;
import java.util.List;
import java.util.UUID;

public record EmprestimoDTORes(
        UUID id,
        UsuarioShortDTORes solicitante,
        List<ItemDTORes> itens,
        String dataHoraInicio,
        String dataHoraFim,
        String observacaoSolicitacao,
        StatusTransacaoItem statusTransacaoItem
) {
}
