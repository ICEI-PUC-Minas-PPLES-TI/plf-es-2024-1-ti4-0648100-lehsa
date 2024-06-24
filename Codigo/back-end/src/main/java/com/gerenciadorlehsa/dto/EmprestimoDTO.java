package com.gerenciadorlehsa.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
@JsonPropertyOrder({"id","dataHoraInicio","dataHoraFim", "solicitante", "endereco","itens","observacaoSolicitacao"})
public record EmprestimoDTO (
        UUID id,
        UsuarioDTO solicitante,
        EnderecoDTO endereco,
        List<ItemDTO> itens,
        String dataHoraInicio,
        String dataHoraFim,
        String observacaoSolicitacao
) {
}
