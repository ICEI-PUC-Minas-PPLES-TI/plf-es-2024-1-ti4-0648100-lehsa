package com.gerenciadorlehsa.service.interfaces;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Emprestimo;
import org.jetbrains.annotations.NotNull;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.dto.SenhaDTO;
import java.util.List;
import java.util.UUID;

/**
 * Entidade com os métodos específicos do UsuarioService
 */
public interface UsuarioService extends PessoaService<User>{

    void atualizarSenha(@NotNull UUID id, @NotNull SenhaDTO senhaDTO);

    boolean existEmail(String email);

    void atualizarPerfil(@NotNull UUID id, Integer code);

    List<Agendamento> listarAgendamentoUsuario(@NotNull UUID id);

    List<Emprestimo> listarEmprestimoUsuario(@NotNull UUID id);

    void removerUsuarioDaListaDeAgendamentos(User user);

    List<String> listarEmailUsuarios();
}
