package com.gerenciadorlehsa.exceptions.lancaveis;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TransacaoException extends RuntimeException{

    public TransacaoException(String message) {
        super(message);
    }
}
