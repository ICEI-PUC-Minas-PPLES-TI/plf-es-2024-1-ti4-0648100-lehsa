package com.gerenciadorlehsa.service.interfaces;

import com.gerenciadorlehsa.security.UserDetailsImpl;
import java.util.UUID;

/**
 * Realiza as validações para ver se um usuário pode realizar uma determinada requisição
 */
public interface Validador {

    boolean validar(UUID id, UserDetailsImpl userDetailsImpl);

    String getTopico();
}
