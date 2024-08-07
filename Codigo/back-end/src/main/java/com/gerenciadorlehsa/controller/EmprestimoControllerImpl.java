package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.service.TransacaoEntityConverterService;
import com.gerenciadorlehsa.controller.interfaces.OperacoesCRUDController;
import com.gerenciadorlehsa.controller.interfaces.TransacaoController;
import com.gerenciadorlehsa.dto.EmprestimoDTO;
import com.gerenciadorlehsa.dto.EmprestimoDTORes;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.repository.EmprestimoRepository;
import com.gerenciadorlehsa.service.MapaTransacaoItemService;
import com.gerenciadorlehsa.service.TransacaoService;
import lombok.AllArgsConstructor;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.util.ConversorEntidadeDTOUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
public class EmprestimoControllerImpl implements OperacoesCRUDController<EmprestimoDTO, EmprestimoDTORes>, TransacaoController{

    private final TransacaoService<Emprestimo, EmprestimoRepository> transacaoService;
    private final OperacoesCRUDService<Emprestimo> operacoesCRUDService;
    private final TransacaoEntityConverterService<Emprestimo, EmprestimoDTO> emprestimoEntityConverterService;
    private final MapaTransacaoItemService<Emprestimo> mapaTransacaoItemService;


    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoDTORes> encontrarPorId (@PathVariable UUID id) {
        log.info(">>> encontrarPorId: recebendo requisição para encontrar emprestimo por id");
        Emprestimo emprestimo = operacoesCRUDService.encontrarPorId(id);
        return ResponseEntity.ok().body(converterParaDtoRes(emprestimo));
    }

    @Override
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar (@Valid @RequestBody EmprestimoDTO obj) {
        log.info(">>> Criar: recebendo requisição para criar emprestimo");

        Emprestimo emprestimo = emprestimoEntityConverterService.convertToEntity(obj);

        emprestimo.setId (null);
        mapaTransacaoItemService.validarMapa (emprestimo);

        Emprestimo emprestimoCriado = operacoesCRUDService.criar(emprestimo);
        return ResponseEntity.created (URI.create("/emprestimo/" + emprestimoCriado.getId())).body (construirRespostaJSON(CHAVES_EMPRESTIMO_CONTROLLER, asList(CREATED.value(), MSG_EMPRESTIMO_CRIADO, emprestimoCriado.getId())));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar (@PathVariable UUID id,
                                                          @Valid @RequestBody EmprestimoDTO obj) {
        log.info(">>> atualizar: recebendo requisição para atualizar emprestimo");

        Emprestimo emprestimo = emprestimoEntityConverterService.convertToEntity(obj);

        emprestimo.setId(id);
        mapaTransacaoItemService.validarMapa (id, emprestimo);

        Emprestimo emprestimoAtt = operacoesCRUDService.atualizar(emprestimo);
        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_EMPRESTIMO_CONTROLLER, asList(OK.value(), MSG_EMPRESTIMO_ATUALIZADO, emprestimoAtt.getId())));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar (@PathVariable UUID id) {
        log.info(">>> deletar: recebendo requisição para deletar emprestimo");
        operacoesCRUDService.deletar(id);
        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_EMPRESTIMO_CONTROLLER, asList(OK.value(), MSG_EMPRESTIMO_DELETADO, id)));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<EmprestimoDTORes>> listarTodos () {
        log.info(">>> listarTodos: recebendo requisição para listar todos emprestimos");
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
