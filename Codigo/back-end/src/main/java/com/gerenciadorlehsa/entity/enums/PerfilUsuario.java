package com.gerenciadorlehsa.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import static com.gerenciadorlehsa.util.ConstantesNumUtil.DOIS;
import static com.gerenciadorlehsa.util.ConstantesNumUtil.UM;
import java.util.Arrays;



@Getter
@AllArgsConstructor
public enum PerfilUsuario {

    ADMIN(UM, "ROLE_ADMIN"),
    USUARIO(DOIS, "ROLE_USUARIO");

    private final Integer codigo;
    private final String descricao;

    /**
     * Obtém o objeto PerfilUsuario relacionado a um código inteiro
     *
     * @param codigo código do perfil
     * @return objeto PerfilUsuario relacionado ao código inteiro
     */
    public static PerfilUsuario getPerfilUsuario(int codigo) {
        return Arrays.stream(PerfilUsuario.values())
                .filter(perfil -> perfil.codigo == codigo)
                .findFirst()
                .orElse(null);
    }
}
