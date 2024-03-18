package com.gerenciadorlehsa.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;


// O sistema já está preparado para mais de um perfil (além do admin)
@AllArgsConstructor
@Getter
public enum ProfileEnum {
    ADMIN(1, "ROLE_ADMIN"),
    USER(2, "ROLE_USER");


    private Integer code;
    private String description;

    public static ProfileEnum toEnum(Integer code) {
        if(Objects.isNull(code))
            return null;

        for(ProfileEnum x : ProfileEnum.values()) {
            if(code.equals(x.getCode()))
                return x;
        }


        throw new IllegalArgumentException("código inválido: " + code);
    }
}
