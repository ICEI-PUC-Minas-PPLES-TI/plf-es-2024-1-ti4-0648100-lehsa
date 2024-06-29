package com.gerenciadorlehsa.util;

import lombok.experimental.UtilityClass;
import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.asList;

/**
 * Contém as constantes relacionadas às requisições
 */
@UtilityClass
public class ConstantesRequisicaoUtil {

    public static final String ENDPOINT_USUARIO = "/usuario";

    public static final String ENDPOINT_PROFESSOR = "/professor";

    public static final String ENDPOINT_AGENDAMENTO = "/agendamento";

    public static final String ENDPOINT_EMPRESTIMO = "/emprestimo";

    public static final String ENDPOINT_ITEM = "/item";

    public static final String[] CAMINHOS_PUBLICOS = {"/", "/actuator/health", "/usuario/verificar-token","/professor" +
            "/confirmacao-cadastro","/professor/professor-confirma"};

    public static final String[] CAMINHOS_PUBLICOS_POST = {"/usuario", "/login"};

    public static final String HEADER_AUTORIZACAO = "Authorization";

    public static final String VALOR_HEADER_AUTORIZACAO = "Bearer %s";

    public static final String TIPO_TOKEN = "Bearer";

    public static final String CONTENT_TYPE = "application/json";

    public static final String CHARACTER_ENCODING = "UTF-8";

    public static final String CORPO_RESPOSTA_REQUISICAO = "{" +
            "\n\"token\": \"" + "%s\"" +

            "\n}";

    public static final String[] PROPRIEDADES_IGNORADAS = new String[]{"id", "password", "perfilUsuario","nota",
            "confirmaCadastro", "dataHoraCriacao", "agendamentos", "emprestimos"};

    public static final List<String> CHAVES_USUARIO_CONTROLLER = new ArrayList<>(asList("status", "mensagem", "id_usuario"));

    public static final List<String> CHAVES_PROFESSOR_CONTROLLER = new ArrayList<>(asList("status", "mensagem",
            "id_professor"));

    public static final List<String> CHAVES_ITEM_CONTROLLER = new ArrayList<>(asList("status", "mensagem",
            "id_item"));

    public static final List<String> CHAVES_AGENDAMENTO_CONTROLLER = new ArrayList<>(asList("status", "mensagem", "id_agendamento"));

    public static final List<String> CHAVES_EMPRESTIMO_CONTROLLER = new ArrayList<>(asList("status", "mensagem", "id_emprestimo"));

    public static final String MSG_USUARIO_CRIADO = "usuario criado com sucesso";

    public static final String MSG_PROFESSOR_CRIADO = "Professor criado com sucesso";

    public static final String MSG_ITEM_CRIADO = "Item criado com sucesso";

    public static final String MSG_AGENDAMENTO_CRIADO = "agendamento criado com sucesso";

    public static final String MSG_AGENDAMENTO_ATUALIZADO = "agendamento atualizado com sucesso";

    public static final String MSG_AGENDAMENTO_DELETADO = "agendamento deletado com sucesso";

    public static final String MSG_AGENDAMENTO_PROFESSOR_CONFIRMA = "Agendamento confirmado pelo professor";

    public static final String MSG_USUARIO_ATUALIZADO = "usuario atualizado com sucesso";

    public static final String MSG_PROFESSOR_ATUALIZADO = "Professor atualizado com sucesso";

    public static final String MSG_ITEM_ATUALIZADO = "Item atualizado com sucesso";

    public static final String MSG_PERFIL_ATUALIZADO = "perfil do usuário atualizado com sucesso";

    public static final String MSG_USUARIO_DELETADO = "usuario deletado com sucesso";

    public static final String MSG_PROFESSOR_DELETADO = "Professor deletado com sucesso";

    public static final String MSG_ITEM_DELETADO = "Item deletado com sucesso";

    public static final String MSG_PROFESSOR_CONFIRMACAO_CADASTRO = "Professor confirmou cadastro";

    public static final String MSG_USUARIO_SENHA = "senha atualizada com sucesso";

    public static final String MSG_EMPRESTIMO_CRIADO = "emprestimo criado com sucesso";
    public static final String MSG_EMPRESTIMO_DELETADO = "emprestimo deletado com sucesso";
    public static final String MSG_EMPRESTIMO_ATUALIZADO = "emprestimo atualizado com sucesso";
}
