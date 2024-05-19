package com.gerenciadorlehsa.exceptions.lancaveis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class AtualizarAgendamentoException extends RuntimeException{

    public AtualizarAgendamentoException (String msg) {
        super(msg);
    }
}
