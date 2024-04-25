package com.gerenciadorlehsa.exceptions.lancaveis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataConflitanteAgendamenteException extends RuntimeException{

    public DataConflitanteAgendamenteException(String message) {
        super(message);
    }
}
