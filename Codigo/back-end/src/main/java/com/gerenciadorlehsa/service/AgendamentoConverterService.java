package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.dto.AgendamentoDTO;
import com.gerenciadorlehsa.dto.ItemDTO;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.exceptions.lancaveis.ItemAgendamentoException;
import com.gerenciadorlehsa.exceptions.lancaveis.SolicitantesAgendamentoException;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
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

        agendamento.setId(agendamentoDTO.id());
        agendamento.setDataHoraInicio(converterDataHora (agendamentoDTO.dataHoraInicio ()));
        agendamento.setDataHoraFim(converterDataHora (agendamentoDTO.dataHoraFim ()));
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
        validarListaSolicitantes (solicitantesDTO);
        return solicitantesDTO.stream()
                .map(UsuarioDTO::email)
                .map(usuarioService::encontrarPorEmail)
                .collect(Collectors.toList());
    }

    private void validarListaSolicitantes(List<UsuarioDTO> solicitantesDTO) {
        if(solicitantesDTO == null || solicitantesDTO.isEmpty ())
            throw new SolicitantesAgendamentoException ("O agendamento tem que ter no mínimo 1 solicitante");
        if (solicitantesDTO.contains(null))
            throw new SolicitantesAgendamentoException("A lista de solicitantes contém elementos nulos");
        if (solicitantesDTO.stream().anyMatch(solicitanteDTO -> solicitanteDTO.email() == null))
            throw new SolicitantesAgendamentoException("A lista de solicitantes contém e-mails nulos");
        if (solicitantesDTO.size() > 10)
            throw new SolicitantesAgendamentoException("O máximo de solicitantes é 10");
    }


    private User encontrarTecnico(String email) {
        if (email != null && !email.isEmpty())
            return usuarioService.encontrarPorEmail(email);
        return null;
    }


    private List<Item> encontrarItens(List<ItemDTO> itensDTO) {
        validarListaItens(itensDTO);

        return itensDTO.stream()
                .map(itemDTO -> itemService.encontrarPorId(itemDTO.id()))
                .collect(Collectors.toList());
    }



    private void validarListaItens(List<ItemDTO> itensDTO) {
        if (itensDTO == null || itensDTO.isEmpty())
            throw new ItemAgendamentoException ("O agendamento tem que ter no mínimo 1 item");
        if (itensDTO.contains(null))
            throw new ItemAgendamentoException("A lista de itens contém elementos nulos");
        if (itensDTO.stream().anyMatch(itemDTO -> itemDTO.id() == null))
            throw new ItemAgendamentoException("A lista de itens contém IDs nulos");
        if (itensDTO.size() > 10)
            throw new ItemAgendamentoException("O máximo de itens é 10");
    }



}
