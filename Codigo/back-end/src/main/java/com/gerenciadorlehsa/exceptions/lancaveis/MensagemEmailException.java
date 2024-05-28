package com.gerenciadorlehsa.exceptions.lancaveis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MensagemEmailException extends RuntimeException{

    public MensagemEmailException(String msg) {
        super(msg);
    }
}
