package com.gerenciadorlehsa.exceptions.lancaveis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflitoDataException extends RuntimeException{

    public ConflitoDataException(String msg) {
        super(msg);
    }
}
