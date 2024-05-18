package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.controller.interfaces.OperacoesCRUDController;
import com.gerenciadorlehsa.controller.interfaces.ProfessorController;
import com.gerenciadorlehsa.dto.ProfessorDTO;
import com.gerenciadorlehsa.entity.Professor;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.ProfessorService;
import com.gerenciadorlehsa.util.ConversorEntidadeDTOUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.*;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.PROFESSOR_CONTROLLER;
import static com.gerenciadorlehsa.util.ConstrutorRespostaJsonUtil.construirRespostaJSON;
import static com.gerenciadorlehsa.util.ConversorEntidadeDTOUtil.converterParaDTO;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@Slf4j(topic = PROFESSOR_CONTROLLER)
@RestController
@Validated
@RequestMapping(ENDPOINT_PROFESSOR)
@AllArgsConstructor
public class ProfessorControllerImpl implements OperacoesCRUDController<Professor, ProfessorDTO>, ProfessorController {

    OperacoesCRUDService<Professor> operacoesCRUDService;

    ProfessorService professorService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> encontrarPorId (@PathVariable UUID id) {
        log.info(">>> encontrarPorId: recebendo requisição para encontrar professor por id");
        Professor professor = operacoesCRUDService.encontrarPorId (id);
        return ResponseEntity.ok().body(converterParaDTO(professor));
    }

    @Override
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar (@Valid @RequestBody Professor professor){
        log.info(">>> criar: recebendo requisição para criar professor");
        Professor professorCriado = operacoesCRUDService.criar(professor);

        return ResponseEntity.created (URI.create("/professor/" + professorCriado.getId())).body (construirRespostaJSON(CHAVES_PROFESSOR_CONTROLLER, asList(CREATED.value(), MSG_PROFESSOR_CRIADO, professorCriado.getId())));

    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar (@PathVariable UUID id,
                                                          @Valid @RequestBody @NotNull Professor professor) {
        log.info(">>> atualizar: recebendo requisição para atualizar professor");

        professor.setId(id);
        Professor professorAtualizado = operacoesCRUDService.atualizar(professor);

        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_PROFESSOR_CONTROLLER, asList(OK.value(),
                MSG_PROFESSOR_ATUALIZADO, professorAtualizado.getId())));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar (@PathVariable UUID id) {
        log.info(">>> deletar: recebendo requisição para deletar professor");
        operacoesCRUDService.deletar(id);

        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_PROFESSOR_CONTROLLER, asList(OK.value(), MSG_PROFESSOR_DELETADO, id)));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> listarTodos () {
        log.info(">>> listarTodos: recebendo requisição para listar todos os professores");

        List<Professor> professores = operacoesCRUDService.listarTodos();
        return ResponseEntity.ok().body(professores.stream().map(ConversorEntidadeDTOUtil ::converterParaDTO).toList());
    }


    @Override
    @PatchMapping("/confirmacao-cadastro")
    public ResponseEntity<Map<String, Object>> confirmarCadastro(@RequestParam("id") UUID id) {
        log.info (" >>> Confirmando o cadastro do professor");
        professorService.confirmaEmail (id, true);
        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_PROFESSOR_CONTROLLER, asList(OK.value(), MSG_PROFESSOR_CONFIRMACAO_CADASTRO, id)));
    }




}
