package com.gerenciadorlehsa.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.ESTILIZAR_EMAIL_UTIL;

@UtilityClass
@Slf4j(topic = ESTILIZAR_EMAIL_UTIL)
public class EstilizacaoEmailUtil {

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


    public static String estilizaConfirmacaoAgendamento(String linkConfirmacao, LocalDateTime inicio, LocalDateTime fim) {
        String dataHoraInicio = DataHoraUtil.converterDataHora (inicio);
        String dataHoraFim = DataHoraUtil.converterDataHora (fim);

        return "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; }" +
                ".container { max-width: 600px; margin: auto; padding: 20px; }" +
                ".header { background-color: #007bff; color: #fff; padding: 10px; text-align: center; }" +
                ".content { padding: 20px; }" +
                ".button { background-color: #007bff; color: #fff; padding: 10px 20px; text-decoration: none; display: inline-block; margin-top: 20px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h2>Confirmação de Agendamento</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Olá,</p>" +
                "<p>Seu agendamento está quase confirmado. Por favor, revise os detalhes abaixo e clique no botão para confirmar seu agendamento:</p>" +
                "<p><strong>Data e Hora de Início:</strong> " + dataHoraInicio + "</p>" +
                "<p><strong>Data e Hora de Fim:</strong> " + dataHoraFim + "</p>" +
                "<a href='" + linkConfirmacao + "' class='button'>Confirmar Agendamento</a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }



}
