package com.gerenciadorlehsa.exceptions.lancaveis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EnumNaoEncontradoException extends RuntimeException{

    public EnumNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
