package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.MensagemEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

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




}
