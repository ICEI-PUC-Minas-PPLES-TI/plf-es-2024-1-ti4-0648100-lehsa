package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.entity.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import com.gerenciadorlehsa.controller.interfaces.UsuarioController;
import com.gerenciadorlehsa.controller.interfaces.OperacoesCRUDController;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.dto.SenhaDTO;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import com.gerenciadorlehsa.util.ConversorEntidadeDTOUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static java.util.Arrays.asList;
import static com.gerenciadorlehsa.util.ConstrutorRespostaJsonUtil.construirRespostaJSON;
import static com.gerenciadorlehsa.util.ConversorEntidadeDTOUtil.converterParaDTO;
import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.*;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.USUARIO_CONTROLLER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j(topic = USUARIO_CONTROLLER)
@RestController
@Validated
@RequestMapping(ENDPOINT_USUARIO)
@AllArgsConstructor
public class UsuarioControllerImpl implements OperacoesCRUDController<User, UsuarioDTO>, UsuarioController {

    private final OperacoesCRUDService<User> operacoesCRUDService;
    private final UsuarioService usuarioService;

    /**
     * Encontra um usuário a partir do seu id
     *
     * @param id id do usuário
     * @return JSON com o usuário encontrado
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> encontrarPorId(@PathVariable UUID id) {
        log.info(">>> encontrarPorId: recebendo requisição para encontrar usuário por id");
        User usuario = operacoesCRUDService.encontrarPorId(id);
        return ResponseEntity.ok().body(converterParaDTO(usuario));
    }

    /**
     * Lista todos os usuários cadastrados
     *
     * @return lista de usuários cadastrados
     */
    @Override
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        log.info(">>> listarTodos: recebendo requisição para listar todos usuários");
        List<User> usuarios = operacoesCRUDService.listarTodos();
        return ResponseEntity.ok().body(usuarios.stream().map(ConversorEntidadeDTOUtil::converterParaDTO).toList());
    }

    /**
     * Cria um novo usuário
     *
     * @param usuario objeto do tipo Usuario
     * @return id do novo usuário criado
     */
    @Override
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(@Valid @RequestBody User usuario) {
        log.info(">>> criar: recebendo requisição para criar usuário");
        User usuarioCriado = operacoesCRUDService.criar(usuario);

        return ResponseEntity.created (URI.create("/usuario/" + usuarioCriado.getId())).body (construirRespostaJSON(CHAVES_USUARIO_CONTROLLER, asList(CREATED.value(), MSG_USUARIO_CRIADO, usuarioCriado.getId())));
    }

    /**
     * Atualiza um usuário previamente cadastrado
     *
     * @param id      id do usuário
     * @param usuario objeto do tipo Usuario (informações atualizadas)
     * @return id do usuário atualizado
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(@PathVariable UUID id,
                                                         @Valid @RequestBody @NotNull User usuario) {
        log.info(">>> atualizar: recebendo requisição para atualizar usuário");
        usuario.setId(id);
        User usuarioAtualizado = operacoesCRUDService.atualizar(usuario);
        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_USUARIO_CONTROLLER, asList(OK.value(), MSG_USUARIO_ATUALIZADO, usuarioAtualizado.getId())));
    }

    /**
     * Deleta um usuário a partir do seu id
     *
     * @param id id do usuário
     * @return id do usuário deletado
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar(@PathVariable UUID id) {
        log.info(">>> deletar: recebendo requisição para deletar usuário");
        operacoesCRUDService.deletar(id);
        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_USUARIO_CONTROLLER, asList(OK.value(), MSG_USUARIO_DELETADO, id)));
    }

    /**
     * Atualiza a senha de um usuário
     *
     * @param id       id do usuário
     * @param senhaDTO objeto do tipo SenhaDTO
     * @return id do usuário atualizado
     */
    @Override
    @PutMapping("/atualizar-senha/{id}")
    public ResponseEntity<Map<String, Object>> atualizarSenha(@PathVariable UUID id, @RequestBody SenhaDTO senhaDTO) {
        log.info(">>> atualizarSenha:  recebendo requisição para atualizar senha de usuário");
        usuarioService.atualizarSenha(id, senhaDTO);
        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_USUARIO_CONTROLLER, asList(OK.value(), MSG_USUARIO_SENHA, id)));
    }
}
