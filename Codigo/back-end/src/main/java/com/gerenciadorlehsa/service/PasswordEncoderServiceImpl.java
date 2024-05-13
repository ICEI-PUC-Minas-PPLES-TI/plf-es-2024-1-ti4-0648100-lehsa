package com.gerenciadorlehsa.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.ENCRIPTADOR_SENHA_SERVICE;

@Slf4j(topic = ENCRIPTADOR_SENHA_SERVICE)
@Service
@AllArgsConstructor
public class PasswordEncoderServiceImpl implements PasswordEncoder {

    private final BCryptPasswordEncoder encoder;

    public PasswordEncoderServiceImpl () {
        this.encoder = new BCryptPasswordEncoder();
    }

    /**
     * Encripta uma senha utilizando BCrypt
     *
     * @param senhaBruta senha a ser encriptada
     * @return senha encriptada
     */
    @Override
    public String encode(CharSequence senhaBruta) {
        log.info(">>> encode: encriptando senha");
        return encoder.encode(senhaBruta);
    }

    /**
     * Verifica se duas senhas são compatíveis, ou seja, se uma senha (não encriptada) é compatível com uma encriptada
     *
     * @param senhaBruta      senha original (não encriptada)
     * @param senhaEncriptada senha encriptada
     * @return boolean indicando se senhas são compatíveis
     */
    @Override
    public boolean matches(CharSequence senhaBruta, String senhaEncriptada) {
        log.info(">>> matches: verificando compatibilidade das senhas");
        return encoder.matches(senhaBruta, senhaEncriptada);
    }
}
