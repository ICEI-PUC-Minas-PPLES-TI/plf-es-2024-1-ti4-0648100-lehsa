package com.gerenciadorlehsa.exceptions.lancaveis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TempoExpiradoException extends RuntimeException{

    public TempoExpiradoException(String msg) {
        super(msg);
    }
}
