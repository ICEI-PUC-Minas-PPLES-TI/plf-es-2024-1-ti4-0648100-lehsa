package com.gerenciadorlehsa.components;

import com.gerenciadorlehsa.dto.AgendamentoDTO;
import com.gerenciadorlehsa.dto.ItemDTO;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.exceptions.lancaveis.ItensAgendamentoException;
import com.gerenciadorlehsa.exceptions.lancaveis.SolicitantesAgendamentoException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_DTO_VALIDADOR_COMP;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_VALIDADOR_COMP;

@Slf4j(topic = AGENDAMENTO_DTO_VALIDADOR_COMP)
@Component
@AllArgsConstructor
@Schema(description = "validações relacionadas aos solicitantes e itens do agendamento")
public class AgendamentoDTOValidadorComp {

    public void validate(AgendamentoDTO agendamentoDTO) {
        log.info (" >>> Validando objeto AgendamentoDTO");
        validarSolicitantes(agendamentoDTO.solicitantes ());
        validarItens(agendamentoDTO.itens ());
    }

    private void validarSolicitantes(List<UsuarioDTO> solicitantesDTO) {

        if(solicitantesDTO == null || solicitantesDTO.isEmpty ())
            throw new SolicitantesAgendamentoException ("O agendamento tem que ter no mínimo 1 solicitante");
        if (solicitantesDTO.contains(null))
            throw new SolicitantesAgendamentoException("A lista de solicitantes contém elementos nulos");
        if (solicitantesDTO.stream().anyMatch(solicitanteDTO -> solicitanteDTO.email() == null))
            throw new SolicitantesAgendamentoException("A lista de solicitantes contém e-mails nulos");
        if (solicitantesDTO.size() > 10)
            throw new SolicitantesAgendamentoException("O máximo de solicitantes é 10");
    }


    private void validarItens(List<ItemDTO> itensDTO) {

        if (itensDTO == null || itensDTO.isEmpty())
            throw new ItensAgendamentoException ("O agendamento tem que ter no mínimo 1 item");
        if (itensDTO.contains(null))
            throw new ItensAgendamentoException ("A lista de itens contém elementos nulos");
        if (itensDTO.stream().anyMatch(itemDTO -> itemDTO.id() == null))
            throw new ItensAgendamentoException ("A lista de itens contém IDs nulos");
        if (itensDTO.size() > 10)
            throw new ItensAgendamentoException ("O máximo de itens é 10");
    }

}
