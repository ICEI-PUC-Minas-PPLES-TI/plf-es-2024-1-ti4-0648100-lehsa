package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.controller.interfaces.OperacoesCRUDController;
import com.gerenciadorlehsa.dto.AgendamentoDTO;
import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.service.AgendamentoConverterService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.*;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_CONTROLLER;
import static com.gerenciadorlehsa.util.ConstrutorRespostaJsonUtil.construirRespostaJSON;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j(topic = AGENDAMENTO_CONTROLLER)
@RestController
@Validated
@RequestMapping(ENDPOINT_AGENDAMENTO)
@AllArgsConstructor
public class AgendamentoControllerImpl implements OperacoesCRUDController<AgendamentoDTO, AgendamentoDTO>{

    private final OperacoesCRUDService<Agendamento> operacoesCRUDService;

    private final AgendamentoConverterService agendamentoConverterService;


    public ResponseEntity<AgendamentoDTO> encontrarPorId (UUID id) {
        return null;
    }


    @PostMapping
    public ResponseEntity<Map<String, Object>> criar (@Valid @RequestBody AgendamentoDTO agendamentoDTO) {

        Agendamento agendamento = agendamentoConverterService.convertToEntity (agendamentoDTO);
        Agendamento agendamentoCriado = operacoesCRUDService.criar (agendamento);

        return ResponseEntity.created (URI.create("/agendamento/" + agendamentoCriado.getId())).body (construirRespostaJSON(CHAVES_USUARIO_CONTROLLER, asList(CREATED.value(), MSG_AGENDAMENTO_CRIADO, agendamentoCriado.getId())));
    }

    @Override
    public ResponseEntity<Map<String, Object>> atualizar (UUID id, AgendamentoDTO obj) {
        return null;
    }


    public ResponseEntity<Map<String, Object>> deletar (UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<List<AgendamentoDTO>> listarTodos () {
        return null;
    }


}
