package com.gerenciadorlehsa.exceptions.lancaveis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProfessorConfirmaCadastroException extends RuntimeException{

    public ProfessorConfirmaCadastroException(String msg) {
        super(msg);
    }
}
