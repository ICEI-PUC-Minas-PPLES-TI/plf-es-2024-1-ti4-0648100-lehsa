package com.gerenciadorlehsa.service.interfaces;

import org.jetbrains.annotations.NotNull;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.dto.SenhaDTO;
import java.util.UUID;

/**
 * Entidade com os métodos específicos do UsuarioService
 */
public interface UsuarioService {

    User encontrarPorEmail(@NotNull String email);
    void atualizarSenha(@NotNull UUID id, @NotNull SenhaDTO senhaDTO);
}