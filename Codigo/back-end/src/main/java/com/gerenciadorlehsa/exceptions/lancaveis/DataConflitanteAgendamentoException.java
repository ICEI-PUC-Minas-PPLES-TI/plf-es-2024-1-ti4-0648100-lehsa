package com.gerenciadorlehsa.exceptions.lancaveis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataConflitanteAgendamentoException extends RuntimeException{

    public DataConflitanteAgendamentoException (String message) {
        super(message);
    }
}
