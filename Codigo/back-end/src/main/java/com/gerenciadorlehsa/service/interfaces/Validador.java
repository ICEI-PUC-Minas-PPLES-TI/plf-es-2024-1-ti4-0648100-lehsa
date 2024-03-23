package com.gerenciadorlehsa.service.interfaces;


import com.gerenciadorlehsa.security.UsuarioDetails;
import java.util.UUID;

/**
 * Realiza as validações para ver se um usuário pode realizar uma determinada requisição
 */
public interface Validador {

    boolean validar(UUID id, UsuarioDetails usuarioDetails);

    String getTopico();
}
