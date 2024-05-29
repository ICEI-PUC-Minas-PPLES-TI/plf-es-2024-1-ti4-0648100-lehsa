package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.controller.interfaces.OperacoesCRUDControllerImg;
import com.gerenciadorlehsa.controller.interfaces.ProfessorController;
import com.gerenciadorlehsa.dto.AgendamentoDTORes;
import com.gerenciadorlehsa.dto.ProfessorDTO;
import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Professor;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDServiceImg;
import com.gerenciadorlehsa.service.interfaces.ProfessorService;
import com.gerenciadorlehsa.util.ConversorEntidadeDTOUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class ProfessorControllerImpl implements OperacoesCRUDControllerImg<Professor, ProfessorDTO>, ProfessorController {

    OperacoesCRUDServiceImg<Professor> operacoesCRUDService;

    ProfessorService professorService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> encontrarPorId (@PathVariable UUID id) {
        log.info(">>> encontrarPorId: recebendo requisição para encontrar professor por id");
        Professor professor = operacoesCRUDService.encontrarPorId (id);
        return ResponseEntity.ok().body(converterParaDTO(professor));
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> criar (@Valid @RequestPart("professor") Professor professor,
                                                      @NotNull @RequestPart("imagem")MultipartFile img) {
        log.info(">>> criar: recebendo requisição para criar professor");
        Professor professorCriado = operacoesCRUDService.criar(professor, img);

        return ResponseEntity.created (URI.create("/professor/" + professorCriado.getId())).body (construirRespostaJSON(CHAVES_PROFESSOR_CONTROLLER, asList(CREATED.value(), MSG_PROFESSOR_CRIADO, professorCriado.getId())));

    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> atualizar (@PathVariable UUID id,
                                                          @Valid @RequestPart("professor") @NotNull Professor professor,
                                                          @RequestPart("imagem") MultipartFile img) {
        log.info(">>> atualizar: recebendo requisição para atualizar professor");

        professor.setId(id);
        Professor professorAtualizado = operacoesCRUDService.atualizar(professor, img);

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


    @Override
    @GetMapping("/{id}/agendamentos")
    public ResponseEntity<List<AgendamentoDTORes>> listarAgendamentoProfessor(@PathVariable UUID id) {
        log.info(">>> listarAgendamento: recebendo requisição para listar todos agendamentos de um professor");

        List<Agendamento> agendamentos =
                professorService.listarAgendamentos (id);

        return ResponseEntity.ok().body(agendamentos.stream().map(ConversorEntidadeDTOUtil::converterParaDtoRes).toList());
    }


    @GetMapping("img/{id}")
    public ResponseEntity<?> encontrarImagemPorId (@PathVariable UUID id) {
        byte [] img = this.operacoesCRUDService.encontrarImagemPorId(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/png"))
                .body(img);
    }

}
