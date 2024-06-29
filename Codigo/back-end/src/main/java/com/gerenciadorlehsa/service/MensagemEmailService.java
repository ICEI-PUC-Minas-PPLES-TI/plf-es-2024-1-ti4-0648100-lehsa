package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.MensagemEmail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Schema(description = "Serviço responsável pelo envio de e-mail")
public class MensagemEmailService {

    private final JavaMailSender javaMailSender;

    @Operation(description = "Envia e-mail a um destinatário")
    public void sendEmail(MensagemEmail mensagemEmail) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage ();

        MimeMessageHelper helper = new MimeMessageHelper (mimeMessage, "utf-8");

        helper.setFrom (mensagemEmail.getRemetente ());
        helper.setSubject (mensagemEmail.getAssunto ());
        helper.setText (mensagemEmail.getMensagem (), true);
        helper.setTo (mensagemEmail.getDestinatarios ()
                .toArray ( new String [mensagemEmail.getDestinatarios ().size ()])
        );

        javaMailSender.send (mimeMessage);
    }


    public void enviarEmailConfirmacaoCadastro(String to, String emailBody) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Confirmação de Cadastro");

        helper.setText(emailBody, true);

        javaMailSender.send(mimeMessage);
    }


    public void enviarEmailConfirmacaoAgendamento(String to, String emailBody) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Confirmação do agendamento");

        helper.setText(emailBody, true);

        javaMailSender.send(mimeMessage);
    }




}
