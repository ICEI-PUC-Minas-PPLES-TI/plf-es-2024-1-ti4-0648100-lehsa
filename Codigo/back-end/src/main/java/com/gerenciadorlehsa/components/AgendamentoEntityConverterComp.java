package com.gerenciadorlehsa.components;

import com.gerenciadorlehsa.components.interfaces.TransacaoDTOValidadadorComp;
import com.gerenciadorlehsa.components.interfaces.TransacaoEntityConverterComp;
import com.gerenciadorlehsa.dto.AgendamentoDTO;
import com.gerenciadorlehsa.dto.ItemDTO;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.exceptions.lancaveis.TransacaoException;
import com.gerenciadorlehsa.service.ItemService;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.gerenciadorlehsa.entity.enums.StatusTransacaoItem.EM_ANALISE;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_ENTITY_CONVERTER_COMP;
import static com.gerenciadorlehsa.util.DataHoraUtil.converterDataHora;

@Slf4j(topic = AGENDAMENTO_ENTITY_CONVERTER_COMP)
@Component
@Schema(description = "Responsável por converter DTO para objeto agendamento")
public class AgendamentoEntityConverterComp extends TransacaoEntityConverterComp<Agendamento,AgendamentoDTO> {

    TransacaoDTOValidadadorComp<AgendamentoDTO> agendamentoDTOValidadadorComp;

    @Autowired
    public AgendamentoEntityConverterComp (UsuarioService usuarioService, ItemService itemService, TransacaoDTOValidadadorComp<AgendamentoDTO> agendamentoDTOValidadadorComp) {
        super (usuarioService, itemService);
        this.agendamentoDTOValidadadorComp = agendamentoDTOValidadadorComp;
    }

    @Override
    public Agendamento convertToEntity(AgendamentoDTO agendamentoDTO) {
        agendamentoDTOValidadadorComp.validate (agendamentoDTO);

        log.info (" >>> Convertendo...");
        Agendamento agendamento = new Agendamento();
        agendamento.setId(agendamentoDTO.id());
        agendamento.setDataHoraInicio(converterDataHora (agendamentoDTO.dataHoraInicio ()));
        agendamento.setDataHoraFim(converterDataHora (agendamentoDTO.dataHoraFim ()));
        agendamento.setObservacaoSolicitacao(agendamentoDTO.observacaoSolicitacao());
        agendamento.setStatusTransacaoItem(EM_ANALISE);
        agendamento.setTecnico(null);
        agendamento.setSolicitantes(acharSolicitantes(agendamentoDTO.solicitantes()));
        agendamento.setItensQuantidade (convertMapa (agendamentoDTO.itens ()));
        return agendamento;
    }


    private List<User> acharSolicitantes(List<UsuarioDTO> solicitantesDTO) {
        List<User> usuarios = new ArrayList<> ();
        for (UsuarioDTO usuarioDTO : solicitantesDTO) {
            usuarios.add (acharSolicitante (usuarioDTO));
        }
        return usuarios;
    }


}
