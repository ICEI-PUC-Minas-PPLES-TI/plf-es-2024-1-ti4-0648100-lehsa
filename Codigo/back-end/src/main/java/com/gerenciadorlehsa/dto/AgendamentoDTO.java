package com.gerenciadorlehsa.dto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import java.util.*;

@Builder
@JsonPropertyOrder({"id","dataHoraInicio","dataHoraFim", "solicitantes", "itens","itensQuantidade", "observacaoSolicitacao"})
public record AgendamentoDTO(
        UUID id,
        List<UsuarioDTO> solicitantes,
        List<ItemDTO> itens,
        String dataHoraInicio,
        String dataHoraFim,
        String observacaoSolicitacao
) {
}
