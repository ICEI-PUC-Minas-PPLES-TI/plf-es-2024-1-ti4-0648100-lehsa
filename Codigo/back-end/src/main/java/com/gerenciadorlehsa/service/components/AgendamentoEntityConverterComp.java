package com.gerenciadorlehsa.service.components;

import com.gerenciadorlehsa.dto.AgendamentoDTO;
import com.gerenciadorlehsa.dto.ItemDTO;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.service.ItemService;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
import static com.gerenciadorlehsa.entity.enums.StatusTransacaoItem.EM_ANALISE;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_ENTITY_CONVERTER_COMP;
import static com.gerenciadorlehsa.util.DataHoraUtil.converterDataHora;

@Slf4j(topic = AGENDAMENTO_ENTITY_CONVERTER_COMP)
@Component
@AllArgsConstructor
public class AgendamentoEntityConverterComp {

    private final AgendamentoValidadorComp agendamentoValidator;
    private final UsuarioService usuarioService;
    private final ItemService itemService;


    public Agendamento convertToEntity(AgendamentoDTO agendamentoDTO) {
        agendamentoValidator.validate(agendamentoDTO);
        return convert(agendamentoDTO);
    }

    public Agendamento convert(AgendamentoDTO agendamentoDTO) {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(agendamentoDTO.id());
        agendamento.setDataHoraInicio(converterDataHora (agendamentoDTO.dataHoraInicio ()));
        agendamento.setDataHoraFim(converterDataHora (agendamentoDTO.dataHoraFim ()));
        agendamento.setObservacaoSolicitacao(agendamentoDTO.observacaoSolicitacao());
        agendamento.setStatusTransacaoItem(EM_ANALISE);
        agendamento.setTecnico(null);
        agendamento.setSolicitantes(acharSolicitantes(agendamentoDTO.solicitantes()));
        agendamento.setItens(acharItens (agendamentoDTO.itens()));
        return agendamento;
    }


    private List<User> acharSolicitantes(List<UsuarioDTO> solicitantesDTO) {
        return solicitantesDTO.stream()
                .map(UsuarioDTO::email)
                .map(usuarioService::encontrarPorEmail)
                .collect(Collectors.toList());
    }


    private List<Item> acharItens(List<ItemDTO> itensDTO) {
        return itensDTO.stream()
                .map(itemDTO -> itemService.encontrarPorId(itemDTO.id()))
                .collect(Collectors.toList());
    }

    public User encontrarUsuario(String email){

        return  usuarioService.encontrarPorEmail(email);
    }

}
