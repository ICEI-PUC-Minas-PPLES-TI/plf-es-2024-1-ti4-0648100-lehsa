package com.gerenciadorlehsa.components;

import com.gerenciadorlehsa.components.abstracts.TransacaoDTOValidadadorComp;
import com.gerenciadorlehsa.dto.AgendamentoDTO;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.exceptions.lancaveis.AgendamentoException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_DTO_VALIDADOR_COMP;

@Slf4j(topic = AGENDAMENTO_DTO_VALIDADOR_COMP)
@Component
@AllArgsConstructor
@Schema(description = "validações relacionadas ao agendamento")
public class AgendamentoDTOValidadorComp extends TransacaoDTOValidadadorComp<AgendamentoDTO> {

    @Override
    public void validate(AgendamentoDTO agendamentoDTO) {
        log.info (" >>> Validando objeto AgendamentoDTO");
        validarSolicitantes(agendamentoDTO.solicitantes ());
        validarItens(agendamentoDTO.itens ());
    }

    private void validarSolicitantes(List<UsuarioDTO> solicitantesDTO) {
        if(solicitantesDTO.size () > 10)
            throw new AgendamentoException ("O agendamento não pode ter mais de 10 solicitantes");
        for (UsuarioDTO usuarioDTO : solicitantesDTO)
            validarSolicitante (usuarioDTO);
    }


}
