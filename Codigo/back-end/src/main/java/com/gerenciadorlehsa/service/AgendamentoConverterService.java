package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.dto.AgendamentoDTO;
import com.gerenciadorlehsa.dto.ItemDTO;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_CONVERTER_SERVICE;
import static com.gerenciadorlehsa.util.DataHoraUtil.converterDataHora;

@Slf4j(topic = AGENDAMENTO_CONVERTER_SERVICE)
@Service
@AllArgsConstructor
public class AgendamentoConverterService {

    private final UsuarioService usuarioService;
    private final ItemService itemService;


    public Agendamento convertToEntity(AgendamentoDTO agendamentoDTO) {
        Agendamento agendamento = new Agendamento();

        LocalDateTime dataHoraInicio = agendamentoDTO.dataHoraInicio () != null ?
                converterDataHora (agendamentoDTO.dataHoraInicio ()) :
                null;

        LocalDateTime dataHoraFim = agendamentoDTO.dataHoraFim () != null ?
                converterDataHora (agendamentoDTO.dataHoraFim ()) :
                null;

        agendamento.setId(agendamentoDTO.id());
        agendamento.setDataHoraInicio(dataHoraInicio);
        agendamento.setDataHoraFim(dataHoraFim);
        agendamento.setObservacaoSolicitacao(agendamentoDTO.observacaoSolicitacao());
        agendamento.setStatusTransacaoItem(agendamentoDTO.statusTransacaoItem());

        User tecnico = encontrarTecnico(agendamentoDTO.tecnico().email());
        agendamento.setTecnico(tecnico);

        List<User> solicitantes = encontrarSolicitantes(agendamentoDTO.solicitantes());
        agendamento.setSolicitantes(solicitantes);

        List<Item> itens = encontrarItens (agendamentoDTO.itens());
        agendamento.setItens(itens);

        return agendamento;
    }

    private List<User> encontrarSolicitantes(List<UsuarioDTO> solicitantesDTO) {
        List<User> solicitantes = new ArrayList<>();
        if (solicitantesDTO != null) {
            for (UsuarioDTO solicitanteDTO : solicitantesDTO) {
                if (solicitanteDTO != null && solicitanteDTO.email() != null) {
                    User solicitante = usuarioService.encontrarPorEmail(solicitanteDTO.email());
                    if (solicitante != null) {
                        solicitantes.add(solicitante);
                    }
                }
            }
        }
        return solicitantes;
    }



    private User encontrarTecnico(String email) {
        if (email != null && !email.isEmpty()) {
            return usuarioService.encontrarPorEmail(email);
        }
        return null;
    }


    private List<Item> encontrarItens(List<ItemDTO> itensDTO) {
        List<Item> itens = new ArrayList<>();
        if (itensDTO != null) {
            for (ItemDTO itemDTO : itensDTO) {
                if (itemDTO != null && itemDTO.id() != null) {
                    // Se alguém não existir, a exceção é lançada
                    Item item = itemService.encontrarPorId(itemDTO.id());

                    if (item != null) {
                        itens.add(item);
                    }
                }
            }
        }
        return itens;
    }


}
