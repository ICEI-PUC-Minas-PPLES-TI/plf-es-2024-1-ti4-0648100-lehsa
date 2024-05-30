package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.components.abstracts.TransacaoEntityConverterComp;
import com.gerenciadorlehsa.controller.interfaces.OperacoesCRUDController;
import com.gerenciadorlehsa.controller.interfaces.TransacaoController;
import com.gerenciadorlehsa.dto.EmprestimoDTO;
import com.gerenciadorlehsa.dto.EmprestimoDTORes;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.util.ConversorEntidadeDTOUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.*;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.EMPRESTIMO_CONTROLLER;
import static com.gerenciadorlehsa.util.ConstrutorRespostaJsonUtil.construirRespostaJSON;
import static com.gerenciadorlehsa.util.ConversorEntidadeDTOUtil.converterParaDtoRes;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j(topic = EMPRESTIMO_CONTROLLER)
@RestController
@Validated
@RequestMapping(ENDPOINT_EMPRESTIMO)
@RequiredArgsConstructor
public class EmprestimoControllerImpl implements OperacoesCRUDController<EmprestimoDTO, EmprestimoDTORes>, TransacaoController, ApplicationEventPublisherAware {

    private final TransacaoService<Emprestimo> transacaoService;
    private final OperacoesCRUDService<Emprestimo> operacoesCRUDService;
    private final TransacaoEntityConverterComp<Emprestimo, EmprestimoDTO> emprestimoEntityConverterComp;

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher (@NotNull ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoDTORes> encontrarPorId (@PathVariable UUID id) {
        Emprestimo emprestimo = operacoesCRUDService.encontrarPorId(id);
        return ResponseEntity.ok().body(converterParaDtoRes(emprestimo));
    }

    @Override
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar (@Valid @RequestBody EmprestimoDTO obj) {
        Emprestimo emprestimo = emprestimoEntityConverterComp.convertToEntity(obj);

        emprestimo.setId (null);
        eventPublisher.publishEvent(generateEvent (emprestimo));

        Emprestimo emprestimoCriado = operacoesCRUDService.criar(emprestimo);
        return ResponseEntity.created (URI.create("/emprestimo/" + emprestimoCriado.getId())).body (construirRespostaJSON(CHAVES_EMPRESTIMO_CONTROLLER, asList(CREATED.value(), MSG_EMPRESTIMO_CRIADO, emprestimoCriado.getId())));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar (@PathVariable UUID id,
                                                          @Valid @RequestBody EmprestimoDTO obj) {
        Emprestimo emprestimo = emprestimoEntityConverterComp.convertToEntity(obj);

        eventPublisher.publishEvent(generateEvent (emprestimo, id));

        emprestimo.setId(id);
        Emprestimo emprestimoAtt = operacoesCRUDService.atualizar(emprestimo);
        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_EMPRESTIMO_CONTROLLER, asList(OK.value(), MSG_EMPRESTIMO_ATUALIZADO, emprestimoAtt.getId())));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar (@PathVariable UUID id) {
        operacoesCRUDService.deletar(id);
        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_EMPRESTIMO_CONTROLLER, asList(OK.value(), MSG_EMPRESTIMO_DELETADO, id)));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<EmprestimoDTORes>> listarTodos () {
        List<Emprestimo> emprestimos = operacoesCRUDService.listarTodos();
        return ResponseEntity.ok().body(emprestimos.stream().map(ConversorEntidadeDTOUtil::converterParaDtoRes).toList());
    }

    @Override
    @PatchMapping("/{id}/{status}")
    public ResponseEntity<Map<String, Object>> atualizarStatus (@PathVariable UUID id,
                                                                @PathVariable String status) {
        log.info(">>> atualizarStatus: recebendo requisição para atualizar status do emprestimo");

        transacaoService.atualizarStatus(status, id);

        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_EMPRESTIMO_CONTROLLER, asList(OK.value(), MSG_EMPRESTIMO_ATUALIZADO, id)));
    }
}
