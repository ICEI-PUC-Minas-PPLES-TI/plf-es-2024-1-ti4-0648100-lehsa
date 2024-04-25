package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gerenciadorlehsa.entity.enums.StatusTransacaoItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

        @NotNull(message = "dataHoraInicio não pode ser nulo")
        String dataHoraInicio,
        @NotNull(message = "dataHoraFim não pode ser nulo")
        String dataHoraFim,

        String observacaoSolicitacao,

        StatusTransacaoItem statusTransacaoItem
) {
}
