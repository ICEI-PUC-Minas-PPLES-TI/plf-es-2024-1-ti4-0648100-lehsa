package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.entity.MensagemEmail;
import com.gerenciadorlehsa.service.MensagemEmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.EMAIL_CONTROLLER;

@Slf4j(topic = EMAIL_CONTROLLER)
@RestController
@AllArgsConstructor
@RequestMapping("/mensagem-email")
public class MensagemEmailController {

    private final MensagemEmailService mensagemEmailService;

    @PostMapping
    public String sendEmail(@RequestBody MensagemEmail mensagemEmail) {
        log.info(">>> enviando e-mail...");

        try {
            mensagemEmailService.sendEmail (mensagemEmail);
            return "Deu Certo!";
        } catch (Exception e) {
            e.printStackTrace ();
            return "Deu Ruim";
        }

    }
}
