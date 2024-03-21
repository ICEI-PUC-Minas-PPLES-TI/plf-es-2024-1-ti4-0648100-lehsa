package com.gerenciadorlehsa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteApiController {

    @GetMapping("/teste-api")
    public String teste() {
        return "Sucesso";
    }

    @GetMapping("/teste-api-bem-vindo")
    public String testeBemVindo(@RequestParam(name = "nome") String nome) {
        return "Olá " + nome + ", Seja bem vindo! ";
    }


}
