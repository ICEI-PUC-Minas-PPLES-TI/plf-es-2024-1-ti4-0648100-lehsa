package com.gerenciadorlehsa.components;

import com.gerenciadorlehsa.dto.AgendamentoDTO;
import com.gerenciadorlehsa.dto.ProfessorDTO;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Professor;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.service.interfaces.ProfessorService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.gerenciadorlehsa.entity.enums.StatusTransacao.EM_ANALISE;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_ENTITY_CONVERTER_COMP;
import static com.gerenciadorlehsa.util.DataHoraUtil.converterDataHora;

@Slf4j(topic = AGENDAMENTO_ENTITY_CONVERTER_COMP)
@Component
@AllArgsConstructor
@Schema(description = "Responsável por converter DTO para objeto agendamento")
public class AgendamentoEntityConverterComp extends TransacaoEntityConverterComp<Agendamento,AgendamentoDTO> {

    private final TransacaoDTOValidadadorComp<AgendamentoDTO> agendamentoDTOValidadadorComp;
    private final ProfessorService professorService;

    @Override
    public Agendamento convertToEntity(AgendamentoDTO agendamentoDTO) {
        agendamentoDTOValidadadorComp.validate (agendamentoDTO);

        log.info (" >>> Convertendo...");
        Agendamento agendamento = new Agendamento();
        agendamento.setId(agendamentoDTO.id());
        agendamento.setDataHoraInicio(converterDataHora (agendamentoDTO.dataHoraInicio ()));
        agendamento.setDataHoraFim(converterDataHora (agendamentoDTO.dataHoraFim ()));
        agendamento.setObservacaoSolicitacao(agendamentoDTO.observacaoSolicitacao());
        agendamento.setStatusTransacao (EM_ANALISE);
        agendamento.setTecnico(null);
        agendamento.setSolicitantes(acharSolicitantes(agendamentoDTO.solicitantes()));
        agendamento.setItensQuantidade (convertMapa (agendamentoDTO.itens ()));
        agendamento.setProfessor (acharProfessor (agendamentoDTO.professor ()));
        return agendamento;
    }


    private List<User> acharSolicitantes(List<UsuarioDTO> solicitantesDTO) {
        return solicitantesDTO.stream()
                .map(this::acharSolicitante)
                .collect(Collectors.toList());
    }

    private Professor acharProfessor(ProfessorDTO professorDTO) {
        return professorService.encontrarPorEmail (professorDTO.email ());
    }


}
