package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@JsonPropertyOrder({"id","dataHoraInicio","dataHoraFim", "solicitantes", "itens","itensQuantidade", "observacaoSolicitacao"})
public record AgendamentoDTO(
        UUID id,
        List<UsuarioDTO> solicitantes,
        List<ItemDTO> itens,
        Map<ItemDTO, Integer> itensQuantidade,
        String dataHoraInicio,
        String dataHoraFim,
        String observacaoSolicitacao
) {
}
