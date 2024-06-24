package com.gerenciadorlehsa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class MensagemEmail {

    private String assunto;
    private String mensagem;
    private String remetente;
    private List<String> destinatarios;
}
