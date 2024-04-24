package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gerenciadorlehsa.entity.enums.StatusTransacaoItem;
import lombok.Builder;
import java.util.List;
import java.util.UUID;

@Builder
@JsonPropertyOrder({"id", "statusTransacaoItem","dataHoraInicio","dataHoraFim","tecnico", "solicitantes", "itens","observacaoSolicitacao"})
public record AgendamentoDTO(
        UUID id,
        UsuarioDTO tecnico,
        List<UsuarioDTO> solicitantes,
        List<ItemDTO> itens,

        String dataHoraInicio,
        String dataHoraFim,

        String observacaoSolicitacao,

        StatusTransacaoItem statusTransacaoItem
) {
}
