package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.exceptions.lancaveis.AtualizarStatusException;
import com.gerenciadorlehsa.service.interfaces.AgendamentoService;
import com.gerenciadorlehsa.exceptions.lancaveis.*;
import com.gerenciadorlehsa.security.UsuarioDetails;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import com.gerenciadorlehsa.dto.SenhaDTO;
import com.gerenciadorlehsa.entity.enums.PerfilUsuario;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.repository.UsuarioRepository;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import org.springframework.stereotype.Service;
import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.PROPRIEDADES_IGNORADAS;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.USUARIO_SERVICE;
import java.util.List;
import java.util.UUID;
import static java.lang.String.format;
import static org.springframework.beans.BeanUtils.copyProperties;


@Slf4j(topic = USUARIO_SERVICE)
@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements OperacoesCRUDService<User>, UsuarioService{

    private final ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;

    private final AgendamentoService agendamentoService;

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoderServiceImpl passwordEncoder;

    /**
     * Encontra um usuário a partir do seu id
     *
     * @param id id do usuário
     * @return usuário encontrado
     */
    @Override
    public User encontrarPorId(@NotNull UUID id) {
        log.info(">>> encontrarPorId: encontrando usuário por id");
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(id, USUARIO_SERVICE);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(format("usuário não encontrado, id: %s", id)));
    }


    /**
     * Cria um novo usuário
     *
     * @param usuario objeto do tipo Usuario
     * @return novo usuário criado
     */
    @Override
    @Transactional
    public User criar(@NotNull User usuario) {
        log.info(">>> criar: criando usuário");
        usuario.setId(null);
        usuario.setPassword (passwordEncoder.encode(usuario.getPassword ()));
        usuario.setPerfilUsuario(PerfilUsuario.USUARIO.getCodigo());
        usuario.setNota (5.0);
        usuario = usuarioRepository.save(usuario);
        log.info(format(">>> criar: usuário criado, id: %s", usuario.getId()));
        return usuario;
    }


    /**
     * Atualiza um usuário previamente cadastrado
     *
     * @param usuario objeto do tipo Usuario (informações atualizadas)
     * @return novo usuário atualizado
     */
    @Override
    public User atualizar(@NotNull User usuario) {
        log.info(">>> atualizar: atualizando usuário");
        User usuarioAtualizado = encontrarPorId(usuario.getId());
        copyProperties(usuario, usuarioAtualizado, PROPRIEDADES_IGNORADAS);

        if (usuarioAtualizado.getPerfilUsuario().equals(PerfilUsuario.ADMIN.getCodigo()))
            usuarioAtualizado.setPerfilUsuario(usuario.getPerfilUsuario());
        usuarioAtualizado = usuarioRepository.save(usuarioAtualizado);

        log.info(format(">>> atualizar: usuário atualizado, id: %s", usuarioAtualizado.getId()));
        return usuarioAtualizado;
    }

    /**
     * Deleta um usuário a partir do seu id
     *
     * @param id id do usuário
     */
    @Override
    @Transactional
    public void deletar(@NotNull UUID id) {
        log.info(">>> deletar: deletando usuário");
        User user = encontrarPorId(id);
        removerUsuarioDaListaDeAgendamentos (user);
        try {
            this.usuarioRepository.deleteById(id);
            log.info(format(">>> deletar: usuário deletado, id: %s", id));
        } catch (Exception e) {
            throw new DeletarEntidadeException(format("existem entidades relacionadas: %s", e));
        }
    }


    /**
     * Encontra um usuário a partir do seu email
     *
     * @param email email do usuário
     * @return usuário encontrado
     */
    @Override
    public User encontrarPorEmail(@NotNull String email) {
        log.info(">>> encontrarPorEmail: encontrando usuário por email");
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(format("usuário não encontrado, email: %s", email)));
    }

    @Override
    public boolean existEmail(String email) {
        return usuarioRepository.existsByEmail (email);
    }


    /**
     * Atualiza senha do usuário
     *
     * @param id       id do usuário
     * @param senhaDTO objeto do tipo SenhaDTO
     */
    @Override
    public void atualizarSenha(@NotNull UUID id, @NotNull SenhaDTO senhaDTO) {
        log.info(">>> atualizarSenha: atualizando senha");
        User usuario = encontrarPorId(id);
        if (passwordEncoder.matches(senhaDTO.senhaOriginal(), usuarioRepository.buscarSenhaUsuarioPorId(usuario.getId())))
            usuarioRepository.atualizarSenhaUsuario(passwordEncoder.encode(senhaDTO.senhaAtualizada()), id);
        else
            throw new AtualizarSenhaException(format("senha original incorreta, id do usuário: %s", id));
    }


    /**
     * Lista todos os usuários criados
     *
     * @return lista de usuários
     */
    @Override
    public List<User> listarTodos() {
        log.info(">>> listarTodos: listando todos usuários");
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        return usuarioRepository.findAll();
    }


    @Override
    public void atualizarPerfil(@NotNull UUID id, Integer code) {
        log.info(">>> atualizarStatus: atualizando status");
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        User usuarioAtulizado = encontrarPorId (id);

        if(!(code > 0 && code < 4))
            throw new AtualizarStatusException ("O Código de perfil do usuário não existe");
        usuarioAtulizado.setPerfilUsuario (code);
        usuarioRepository.save (usuarioAtulizado);
    }

    public void removerUsuarioDaListaDeAgendamentos(User user) {
        List<Agendamento> agendamentos = user.getAgendamentosRealizados();

        if(agendamentos != null && !agendamentos.isEmpty ()) {

            for (Agendamento agendamento : agendamentos) {

                agendamento.getSolicitantes().remove(user);

                if (agendamento.getSolicitantes().isEmpty()) {
                    agendamentoService.deletarAgendamentoSeVazio (agendamento.getId ());
                }
            }
        }
    }

    @Override
    public List<String> listarEmailUsuarios () {
        log.info(">>> listarEmailUsuarios: listando email de usuarios");
        return this.usuarioRepository.findEmailUsuarios();
    }

    @Override
    public List<Agendamento> listarAgendamentoUsuario (@NotNull UUID id) {
        User usuario = encontrarPorId(id);
        log.info(">>> listarAgendamentoUsuario: listando todos agendamentos do usuario de id: " + usuario.getId());
        UsuarioDetails usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();
        if (usuarioLogado.getId().compareTo(usuario.getId()) == 0 || usuarioLogado.getPerfilUsuario().getCodigo() == 1)
            return this.usuarioRepository.findAgendamentosRealizadosById(id);

        throw new UsuarioNaoAutorizadoException("O usuário não possui permissão para ver esses agendamentos");
    }

    @Override
    public List<Emprestimo> listarEmprestimoUsuario (@NotNull UUID id) {
        User usuario = encontrarPorId(id);
        log.info(">>> listarEmprestimoUsuario: listando todos emprestimo do usuario de id: " + usuario.getId());
        UsuarioDetails usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();
        if (usuarioLogado.getId().compareTo(usuario.getId()) == 0 || usuarioLogado.getPerfilUsuario().getCodigo() == 1)
            return this.usuarioRepository.findEmprestimosById(id);
        throw new UsuarioNaoAutorizadoException("O usuário não possui permissão para ver esses emprestimos");
    }
}
