package com.gerenciadorlehsa.service.validadores;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import com.gerenciadorlehsa.security.UsuarioDetails;
import com.gerenciadorlehsa.service.interfaces.Validador;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.USUARIO_SERVICE;
import java.util.UUID;

@AllArgsConstructor
public class UsuarioServiceValidadorImpl implements Validador {

    /**
     * Valida um usuário para efetuar uma requisição
     *
     * @param idUsuario     id do usuário relativo à requisição
     * @param usuarioDetails objeto do tipo usuarioDetails
     * @return boolean indicando se o usuário foi ou não validado
     */
    @Override
    public boolean validar(UUID idUsuario, @NotNull UsuarioDetails usuarioDetails) {
        return usuarioDetails.getId().equals(idUsuario);
    }

    /**
     * Obtém o tópico do validador
     *
     * @return tópico
     */
    @Override
    public String getTopico() {
        return USUARIO_SERVICE;
    }
}
