package com.gerenciadorlehsa.exceptions.lancaveis;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AtualizarStatusException extends RuntimeException{

    public AtualizarStatusException(String message) {
        super(message);
    }
}
