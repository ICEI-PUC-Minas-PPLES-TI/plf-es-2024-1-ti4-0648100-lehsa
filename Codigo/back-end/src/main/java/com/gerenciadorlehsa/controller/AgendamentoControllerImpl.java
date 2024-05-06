package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.controller.interfaces.OperacoesCRUDController;
import com.gerenciadorlehsa.dto.AgendamentoDTO;
import com.gerenciadorlehsa.dto.AgendamentoDTORes;
import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.service.TransacaoItemService;
import com.gerenciadorlehsa.service.components.AgendamentoEntityConverterComp;
import com.gerenciadorlehsa.service.interfaces.AgendamentoService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.util.ConversorEntidadeDTOUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.*;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_CONTROLLER;
import static com.gerenciadorlehsa.util.ConstrutorRespostaJsonUtil.construirRespostaJSON;
import static com.gerenciadorlehsa.util.ConversorEntidadeDTOUtil.converterParaDtoRes;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@Slf4j(topic = AGENDAMENTO_CONTROLLER)
@RestController
@Validated
@RequestMapping(ENDPOINT_AGENDAMENTO)
@AllArgsConstructor
public class AgendamentoControllerImpl implements OperacoesCRUDController<AgendamentoDTO, AgendamentoDTORes> {

    private final OperacoesCRUDService<Agendamento> operacoesCRUDService;
    private final AgendamentoService agendamentoService;
    private final AgendamentoEntityConverterComp agendamentoEntityConverterComp;
    private final TransacaoItemService<Agendamento> transacaoItemService;


    @Override
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoDTORes> encontrarPorId (@PathVariable  UUID id) {
        log.info(">>> encontrarPorId: recebendo requisição para encontrar usuário por id");
        Agendamento agendamento = operacoesCRUDService.encontrarPorId(id);
        return ResponseEntity.ok().body(converterParaDtoRes (agendamento));
    }


    @Override
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar (@Valid @RequestBody AgendamentoDTO agendamentoDTO) {
        log.info(">>> criar: recebendo requisição para criar agendamento");
        Agendamento agendamento = agendamentoEntityConverterComp.convertToEntity (agendamentoDTO);
        Agendamento agendamentoCriado = operacoesCRUDService.criar (agendamento);

        return ResponseEntity.created (URI.create("/agendamento/" + agendamentoCriado.getId())).body (construirRespostaJSON(CHAVES_AGENDAMENTO_CONTROLLER, asList(CREATED.value(), MSG_AGENDAMENTO_CRIADO, agendamentoCriado.getId())));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar (@PathVariable UUID id,
                                                          @Valid @RequestBody AgendamentoDTO obj) {
        log.info(">>> atualizar: recebendo requisição para atualizar agendamento");
        Agendamento agendamento = agendamentoEntityConverterComp.convertToEntity (obj);
        agendamento.setId(id);
        Agendamento agendamentoAtt = operacoesCRUDService.atualizar(agendamento);

        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_AGENDAMENTO_CONTROLLER, asList(OK.value(), MSG_AGENDAMENTO_ATUALIZADO, agendamentoAtt.getId())));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar (@PathVariable UUID id) {
        log.info(">>> deletar: recebendo requisição para deletar agendamento");

        operacoesCRUDService.deletar(id);
        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_AGENDAMENTO_CONTROLLER, asList(OK.value(), MSG_AGENDAMENTO_DELETADO, id)));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<AgendamentoDTORes>> listarTodos () {
        log.info(">>> listarTodos: recebendo requisição para listar todos agendamentos");
        List<Agendamento> agendamentos = this.operacoesCRUDService.listarTodos();

        return ResponseEntity.ok().body(agendamentos.stream().map(ConversorEntidadeDTOUtil::converterParaDtoRes).toList());
    }

    @GetMapping("/usuario/{email}")
    public ResponseEntity<List<AgendamentoDTORes>> listarAgendamentoUsuario (@PathVariable String email) {
        log.info(">>> listarAgendamentoUsuario: recebendo requisição para listar todos agendamentos de um usuario");
        List<Agendamento> agendamentos =
                this.agendamentoService.listarAgendamentoUsuario(this.agendamentoEntityConverterComp.encontrarUsuario(email));

        return ResponseEntity.ok().body(agendamentos.stream().map(ConversorEntidadeDTOUtil::converterParaDtoRes).toList());
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<Map<String, Object>> atualizarStatus (@PathVariable UUID id,
                                                                @PathVariable String status) {
        log.info(">>> atualizarStatus: recebendo requisição para atualizar status do agendamento");

        transacaoItemService.atualizarStatus(status, id);

        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_AGENDAMENTO_CONTROLLER, asList(OK.value(), MSG_AGENDAMENTO_ATUALIZADO, id)));
    }

    @PatchMapping("/tecnico/{id}/{email}")
    public ResponseEntity<Map<String, Object>> atualizarTecnico (@PathVariable UUID id,
                                                                @PathVariable String email) {
        log.info(">>> atualizarTecnico: recebendo requisição para atualizar tecnico do agendamento");

        agendamentoService.atualizarTecnico(agendamentoEntityConverterComp.encontrarUsuario(email), id);

        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_AGENDAMENTO_CONTROLLER, asList(OK.value(), MSG_AGENDAMENTO_ATUALIZADO, id)));
    }
}
