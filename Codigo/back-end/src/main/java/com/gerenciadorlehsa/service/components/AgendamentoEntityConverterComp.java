package com.gerenciadorlehsa.service.components;

import com.gerenciadorlehsa.dto.AgendamentoDTO;
import com.gerenciadorlehsa.dto.ItemDTO;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.service.ItemService;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.gerenciadorlehsa.entity.enums.StatusTransacaoItem.EM_ANALISE;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_ENTITY_CONVERTER_COMP;
import static com.gerenciadorlehsa.util.DataHoraUtil.converterDataHora;

@Slf4j(topic = AGENDAMENTO_ENTITY_CONVERTER_COMP)
@Component
@AllArgsConstructor
@Schema(description = "Responsável por converter DTO para objeto agendamento")
public class AgendamentoEntityConverterComp {

    private final AgendamentoValidadorComp agendamentoValidator;
    private final ValidadorTransacaoComp validadorTransacaoComp;
    private final UsuarioService usuarioService;
    private final ItemService itemService;


    public Agendamento convertToEntity(AgendamentoDTO agendamentoDTO) {
        agendamentoValidator.validate(agendamentoDTO);
        Agendamento agendamento = convert (agendamentoDTO);
        validadorTransacaoComp.validarTransacao (agendamento);
        return agendamento;
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
        agendamento.setItensQuantidade (convertMapa (agendamentoDTO.itens ()));
       /* agendamento.setItens(acharItens (agendamentoDTO.itens()));
        agendamento.setItensQuantidade (convertMapa (agendamentoDTO.itens (), agendamento.getItens ()));*/


        return agendamento;
    }

    private Map<Item, Integer> convertMapa(List<ItemDTO> itemDTOS) {
        List<Integer> quantidade = itemDTOS
                .stream ()
                .map (ItemDTO::quantidadeTransacao)
                .toList ();
        List<Item> chaves = acharItens (itemDTOS);

        Map<Item, Integer> mapa = new HashMap<> ();
        if (chaves.size() == quantidade.size()) {
            for (int i = 0; i < chaves.size (); i++) {
                Item chave = chaves.get (i);
                Integer valor = quantidade.get (i);
                mapa.put (chave, valor);
            }
        }
        return mapa;
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
