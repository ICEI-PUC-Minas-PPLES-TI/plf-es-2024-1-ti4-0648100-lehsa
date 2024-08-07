package com.gerenciadorlehsa.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.UtilityClass;


@UtilityClass
@Schema(description = "Contém as constantes relacionadas aos tópicos das classes")
public class ConstantesTopicosUtil {

    public static final String USUARIO_SERVICE = "USUARIO_SERVICE";

    public static final String AGENDAMENTO_SERVICE = "AGENDAMENTO_SERVICE";

    public static final String PROFESSOR_SERVICE = "PROFESSOR_SERVICE";

    public static final String MAPA_TRANSACAO_ITEM_SERVICE = "MAPA_TRANSACAO_ITEM_SERVICE";

    public static final String EMPRESTIMO_SERVICE = "EMPRESTIMO_SERVICE";

    public static final String TRANSACAO_SERVICE = "TRANSACAO_SERVICE";

    public static final String AGENDAMENTO_ENTITY_CONVERTER_COMP = "AGENDAMENTO_ENTITY_CONVERTER_COMP";

    public static final String EMPRESTIMO_ENTITY_CONVERTER_COMP = "EMPRESTIMO_ENTITY_CONVERTER_COMP";

    public static final String AGENDAMENTO_DTO_VALIDADOR_COMP = "AGENDAMENTO_DTO_VALIDADOR_COMP";

    public static final String EMPRESTIMO_DTO_VALIDADOR_COMP = "EMPRESTIMO_DTO_VALIDADOR_COMP";

    public static final String ITEM_SERVICE = "ITEM_SERVICE";


    public static final String VALIDADOR_AUTORIZACAO_REQUISICAO_SERVICE = "VALIDADOR_AUTORIZACAO_REQUISICAO_SERVICE";

    public static final String USUARIO_SPRING_SECURITY_SERVICE = "USUARIO_SPRING_SECURITY_SERVICE";

    public static final String ENCRIPTADOR_SENHA_SERVICE = "ENCRIPTADOR_SENHA_SERVICE";

    public static final String USUARIO_CONTROLLER = "USUARIO_CONTROLLER";

    public static final String PROFESSOR_CONTROLLER = "PROFESSOR_CONTROLLER";

    public static final String EMAIL_CONTROLLER = "EMAIL_CONTROLLER";

    public static final String AGENDAMENTO_CONTROLLER = "AGENDAMENTO_CONTROLLER";

    public static final String EMPRESTIMO_CONTROLLER = "EMPRESTIMO_CONTROLLER";

    public static final String ITEM_CONTROLLER = "ITEM_CONTROLLER";


    public static final String JWT_COMP = "JWT_COMP";

    public static final String SEGURANCA_CONFIG = "SEGURANCA_CONFIG";

    public static final String INTERCEPTADOR_EXCECOES = "INTERCEPTADOR_EXCECOES";

    public static final String JWT_FILTRO_AUTENTICACAO = "JWT_FILTRO_AUTENTICACAO";

    public static final String JWT_FILTRO_AUTORIZACAO = "JWT_FILTRO_AUTORIZACAO";

    public static final String CONVERSOR_ENTIDADE_DTO_UTIL = "CONVERSOR_ENTIDADE_DTO_UTIL";

    public static final String DATA_HORA_UTIL = "DATA_HORA_UTIL";

    public static final String ESTILIZAR_EMAIL_UTIL = "ESTILIZAR_EMAIL_UTIL";

    public static final String AGENDAMENTO_EVENT_LISTENER = "AGENDAMENTO_EVENT_LISTENER";

    public static final String ITEM_EVENT_LISTENER = "ITEM_EVENT_LISTENER";

    public static final String USUARIO_EVENT_LISTENER = "USUARIO_EVENT_LISTENER";
}