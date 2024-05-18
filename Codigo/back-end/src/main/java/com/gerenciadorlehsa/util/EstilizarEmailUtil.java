package com.gerenciadorlehsa.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.DATA_HORA_UTIL;

@UtilityClass
@Slf4j(topic = DATA_HORA_UTIL)
public class EstilizarEmailUtil {

    public static String estilizaConfirmacao (String linkConfirmacao) {
        return "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; }" +
                ".container { max-width: 600px; margin: auto; padding: 20px; }" +
                ".header { background-color: #007bff; color: #fff; padding: 10px; text-align: center; }" +
                ".content { padding: 20px; }" +
                ".button { background-color: #007bff; color: #fff; padding: 10px 20px; text-decoration: none; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h2>Bem-vindo ao nosso sistema!</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Olá,</p>" +
                "<p>Você se cadastrou no nosso sistema. Por favor, clique no botão abaixo para confirmar o seu cadastro:</p>" +
                "<a href='" + linkConfirmacao + "' class='button'>Confirmar Cadastro</a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

}
