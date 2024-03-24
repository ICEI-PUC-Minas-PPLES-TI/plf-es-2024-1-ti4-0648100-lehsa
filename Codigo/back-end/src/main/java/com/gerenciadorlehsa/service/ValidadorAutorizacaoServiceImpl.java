package com.gerenciadorlehsa.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.gerenciadorlehsa.entity.enums.PerfilUsuario;
import com.gerenciadorlehsa.exceptions.lancaveis.TopicoNaoEncontradoException;
import com.gerenciadorlehsa.exceptions.lancaveis.UsuarioNaoAutorizadoException;
import com.gerenciadorlehsa.security.UsuarioDetails;
import com.gerenciadorlehsa.service.interfaces.Validador;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import com.gerenciadorlehsa.service.validadores.UsuarioServiceValidadorImpl;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.VALIDADOR_AUTORIZACAO_REQUISICAO_SERVICE;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j(topic = VALIDADOR_AUTORIZACAO_REQUISICAO_SERVICE)
@Service
public class ValidadorAutorizacaoServiceImpl implements ValidadorAutorizacaoRequisicaoService {

    private final List<Validador> validadores;

    private ValidadorAutorizacaoServiceImpl() {
        this.validadores = new ArrayList<>(asList(
                new UsuarioServiceValidadorImpl()));
    }

    /**
     * Verifica as autorizações de um usuário
     * Essa versão do método suporta que uma requisição seja realizada por usuários não que não possuam perfil ADMIN
     *
     * @param id id do objeto do tipo Usuario relacionado à requisição
     * @return usuário autorizado
     */
    @Override
    public UsuarioDetails validarAutorizacaoRequisicao(UUID id, String topico) {

        UsuarioDetails usuarioDetails = autenticar();

        boolean usuarioAutorizado = validadores.stream().
                filter(validador -> validador.getTopico().equals(topico))
                .findFirst()
                .orElseThrow(() -> new TopicoNaoEncontradoException(format("tópico não encontrado: %s", topico)))
                .validar(id, usuarioDetails);

        if (!(usuarioEhAdmin(requireNonNull(usuarioDetails)) || usuarioAutorizado))
            throw new UsuarioNaoAutorizadoException(format("usuário [%s] não possui autorização para utilizar esse método", usuarioDetails.getUsername()));

        log.info(format(">>> validarAutorizacaoRequisicao: usuário [%s] autorizado para realizar requisição", usuarioDetails.getUsername()));
        return usuarioDetails;
    }

    /**
     * Verifica as autorizações de um usuário
     * Essa versão do método bloqueia qualquer requisição por usuários não ADMINS
     *
     * @return usuário autorizado
     */
    @Override
    public UsuarioDetails validarAutorizacaoRequisicao() {

        UsuarioDetails usuarioDetails = autenticar();

        if (!usuarioEhAdmin(requireNonNull(usuarioDetails)))
            throw new UsuarioNaoAutorizadoException(format("usuário [%s] não possui autorização para utilizar esse método", usuarioDetails.getUsername()));

        log.info(format(">>> validarAutorizacaoRequisicao: usuário [%s] autorizado para realizar requisição", usuarioDetails.getUsername()));
        return usuarioDetails;
    }

    /**
     * Autentica usuário
     *
     * @return usuário autenticado, caso contrário null
     */
    private @Nullable UsuarioDetails autenticar() {
        try {
            return (UsuarioDetails) getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UsuarioNaoAutorizadoException("usuário não logado");
        }
    }

    /**
     * Verifica se um usuário é administrador
     *
     * @param userSpringSecurity usuário
     * @return boolean indicando se usuário é administrador ou não
     */
    private boolean usuarioEhAdmin(@NotNull UsuarioDetails userSpringSecurity) {
        return userSpringSecurity.ehPerfil(PerfilUsuario.ADMIN);
    }
}
