package com.gerenciadorlehsa.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import com.gerenciadorlehsa.exceptions.lancaveis.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.io.IOException;
import static com.gerenciadorlehsa.util.ConstantesErroValidadorUtil.MSG_ERRO_USUARIO_SENHA;
import static com.gerenciadorlehsa.util.ConstantesErroValidadorUtil.MSG_ERRO_VALIDACAO;
import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.CONTENT_TYPE;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.INTERCEPTADOR_EXCECOES;
import static java.lang.String.format;

@Slf4j(topic = INTERCEPTADOR_EXCECOES)
@Component
@RestControllerAdvice
public class InterceptadorExcecoes extends DefaultHandlerExceptionResolver implements AuthenticationFailureHandler {

    @Value("${server.error.include-exception}")
    private boolean imprimirStackTrace;

    /**
     * Captura exceções do tipo AuthenticationException
     *
     * @param request   requisição
     * @param response  resposta
     * @param exception exceção
     * @throws IOException lança exceção caso haja problema na conversão dos dados
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, @NotNull HttpServletResponse response, AuthenticationException exception) throws IOException {
        int statusHttp = HttpStatus.UNAUTHORIZED.value();
        RespostaErro respostaErro = new RespostaErro(statusHttp, MSG_ERRO_USUARIO_SENHA);
        response.setStatus(statusHttp);
        response.setContentType(CONTENT_TYPE);
        response.getWriter().append(respostaErro.toJson());
    }

    /**
     * Captura exceções do tipo MethodArgumentNotValidException
     *
     * @param e       exceção do tipo MethodArgumentNotValidException
     * @param request requisição
     * @return tratamento da exceção (log e resposta requisição)
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> capturarMethodArgumentNotValidException(@NotNull MethodArgumentNotValidException e, WebRequest request) {
        RespostaErro respostaErro = new RespostaErro(HttpStatus.UNPROCESSABLE_ENTITY.value(), MSG_ERRO_VALIDACAO);
        for (FieldError fieldError : e.getBindingResult().getFieldErrors())
            respostaErro.addErroValidacao(fieldError.getField(), fieldError.getDefaultMessage());
        return ResponseEntity.unprocessableEntity().body(respostaErro);
    }

    /**
     * Captura exceções do tipo Exception (método geral para capturar qualquer exceção não tratada)
     *
     * @param e       exceção do tipo Exception
     * @param request requisição
     * @return tratamento da exceção (log e resposta requisição)
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, TopicoNaoEncontradoException.class, ConstrutorRespostaJsonException.class})
    public ResponseEntity<Object> capturarException(@NotNull Exception e, WebRequest request) {
        final String msgErro = e.getMessage();
        log.error(format("[ERRO] Exception: erro desconhecido ocorrido: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Captura exceções do tipo DataIntegrityViolationException
     *
     * @param e       exceção do tipo DataIntegrityViolationException
     * @param request requisição
     * @return tratamento da exceção (log e resposta requisição)
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> capturarDataIntegrityViolationException(@NotNull DataIntegrityViolationException e, WebRequest request) {
        String msgErro = e.getMostSpecificCause().getMessage();
        log.error(format("[ERRO] DataIntegrityViolationException: falha para salvar a entidade: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.CONFLICT, request);
    }

    /**
     * Captura exceções do tipo AtualizarSenhaException
     *
     * @param e       exceção do tipo AtualizarSenhaException
     * @param request requisição
     * @return tratamento da exceção (log e resposta requisição)
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AtualizarSenhaException.class)
    public ResponseEntity<Object> capturarAtualizarSenhaException(@NotNull AtualizarSenhaException e, WebRequest request) {
        String msgErro = e.getMostSpecificCause().getMessage();
        log.error(format("[ERRO] AtualizarSenhaException: falha para atualizar a senha: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.CONFLICT, request);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TransacaoException.class)
    public ResponseEntity<Object> capturarTransacaoException(@NotNull TransacaoException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] TransacaoException: falha em transação: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ItemException.class)
    public ResponseEntity<Object> capturarTransacaoException(@NotNull ItemException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] ItemException: falha no item: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.BAD_REQUEST, request);
    }



    /**
     * Captura exceções do tipo DataIntegrityViolationException
     *
     * @param e       exceção do tipo DataIntegrityViolationException
     * @param request requisição
     * @return tratamento da exceção (log e resposta requisição)
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DeletarEntidadeException.class)
    public ResponseEntity<Object> capturarDeletarEntidadeException(@NotNull DeletarEntidadeException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] DeletarEntidadeException: falha ao deletar a entidade: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.CONFLICT, request);
    }


    /**
     * Captura exceção do tipo AtualizarStatusException
     * @param e Exceção do tipo AtualizarStatusException
     * @param request requisição
     * @return tratamento da exceção (log e resposta da requisição)
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AtualizarStatusException.class)
    public ResponseEntity<Object> capturarAtualizarStatusException(@NotNull AtualizarStatusException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] AtualizarStatusException: falha ao atualizar status do usuário: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.CONFLICT, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataException.class)
    public ResponseEntity<Object> capturarDataException(@NotNull DataException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] DataException: falha ao escolher data: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MensagemEmailException.class)
    public ResponseEntity<Object> capturarMensagemEmailException(@NotNull MensagemEmailException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] MensagemEmailExeption: falha no envio de e-mail: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ProfessorConfirmaCadastroException.class)
    public ResponseEntity<Object> capturarProfessorConfirmaCadastroException(@NotNull ProfessorConfirmaCadastroException e,WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] ProfessorConfirmaCadastroException: falha na confirmação de e-mail do professor: %s",
                msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.CONFLICT, request);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProfessorException.class)
    public ResponseEntity<Object> capturarProfessorException(@NotNull ProfessorException e,WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] ProfessorException: recurso do professor não encontrado: %s",
                msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.NOT_FOUND, request);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(TempoExpiradoException.class)
    public ResponseEntity<Object> capturarTempoExpiradoException(@NotNull TempoExpiradoException e,WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] TempoExpiradoException: Tempo esgotado: %s",
                msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.CONFLICT, request);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflitoDataException.class)
    public ResponseEntity<Object> capturarConflitoDataException(@NotNull ConflitoDataException e,WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] ConflitoDataException: Data conflitante: %s",
                msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.CONFLICT, request);
    }



    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(AtualizarAgendamentoException.class)
    public ResponseEntity<Object> capturarAtualiazarAgendamentoException(@NotNull AtualizarAgendamentoException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] AtualizarAgendamentoException: Falha ao atualizar o agendamento: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ProfessorConfirmaAgendamentoException.class)
    public ResponseEntity<Object> capturarProfessorConfirmaAgendamentoException(@NotNull ProfessorConfirmaAgendamentoException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] ProfessorConfirmaAgendamentoException: falha na confirmação de agendamento por " +
                        "parte do professor: " +
                        "%s",
                msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.CONFLICT, request);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AgendamentoException.class)
    public ResponseEntity<Object> capturarAgendamentoException(@NotNull AgendamentoException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] AgendamentoException: erro no agendamento: " +
                "%s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.BAD_REQUEST, request);
    }


    /**
     * Captura exceções do tipo UsuarioNaoAutorizadoException
     *
     * @param e       exceção do tipo UsuarioNaoAutorizadoException
     * @param request requisição
     * @return tratamento da exceção (log e resposta requisição)
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UsuarioNaoAutorizadoException.class)
    public ResponseEntity<Object> capturarUsuarioNaoAutorizadoException(@NotNull UsuarioNaoAutorizadoException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] UsuarioNaoAutorizadoException: falha na autorização do usuário: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.FORBIDDEN, request);
    }

    /**
     * Captura exceções do tipo EntidadeNaoEncontradaException
     *
     * @param e       exceção do tipo EntidadeNaoEncontradaException
     * @param request requisição
     * @return tratamento da exceção (log e resposta requisição)
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<Object> capturarEntidadeNaoEncontradaException(@NotNull EntidadeNaoEncontradaException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] EntidadeNaoEncontradaException: entidade não encontrada: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.NOT_FOUND, request);
    }

    /**
     * Captura exceções do tipo MethodArgumentTypeMismatchException e HttpMessageNotReadableException
     *
     * @param e       exceção do tipo MethodArgumentTypeMismatchException ou HttpMessageNotReadableException
     * @param request requisição
     * @return tratamento da exceção (log e resposta requisição)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Object> capturarMethodArgumentTypeMismatchExceptionEHttpMessageNotReadableException(@NotNull Exception e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] %s: erro ao converter valor de string: %s", e.getClass().getSimpleName(), msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.BAD_REQUEST, request);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({EntidadesRelacionadasException.class})
    public ResponseEntity<Object> capturarEntidadesRelacionadasException(@NotNull EntidadesRelacionadasException e, WebRequest request) {
        final String msgErro = e.getMessage();
        log.error(format("[ERRO] EntidadesRelacionadasException: erro desconhecido ocorrido: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.CONFLICT, request);
    }


    /**
     * Captura exceções do tipo EnumNaoEncontradoException
     *
     * @param e       exceção do tipo EnumNaoEncontradoException
     * @param request requisição
     * @return tratamento da exceção (log e resposta requisição)
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EnumNaoEncontradoException.class)
    public ResponseEntity<Object> capturarEnumNaoEncontradoException(@NotNull EnumNaoEncontradoException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] EnumNaoEncontradoException: enum não encontrado: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.NOT_FOUND, request);
    }

    /**
     * Captura exceções do tipo NoResoureceFoundException
     *
     * @param e       exceção do tipo NoResoureceFoundException
     * @param request requisição
     * @return tratamento da exceção (log e resposta requisição)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> capturarNoResoureceFoundException(@NotNull NoResourceFoundException e, WebRequest request) {
        String msgErro = e.getMessage().split("resource", 2)[1];
        msgErro = "o recurso acessado não existe:" + msgErro;
        log.error(format("[ERRO] %s: %s", e.getClass().getSimpleName(),msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmprestimoException.class)
    public ResponseEntity<Object> capturarEmprestimoException(@NotNull EmprestimoException e, WebRequest request) {
        String msgErro = e.getMessage();
        log.error(format("[ERRO] EmprestimoException: erro no empréstimo: %s", msgErro));
        return construirMsgErro(e, msgErro, HttpStatus.BAD_REQUEST, request);
    }


    /**
     * Constrói mensagem de erro (4 parâmetros)
     *
     * @param e          exceção
     * @param message    mensagem de erro
     * @param httpStatus status http
     * @param request    requisição
     * @return mensagem de erro da requisição
     */
    private @NotNull ResponseEntity<Object> construirMsgErro(Exception e, String message, @NotNull HttpStatus httpStatus, WebRequest request) {
        RespostaErro respostaErro = new RespostaErro(httpStatus.value(), message);
        if (imprimirStackTrace)
            respostaErro.setStackTrace(ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(httpStatus).body(respostaErro);
    }

    /**
     * Constrói mensagem de erro (3 parâmetros)
     *
     * @param e          exceção
     * @param httpStatus status http
     * @param request    requisição
     * @return mensagem de erro da requisição
     */
    private @NotNull ResponseEntity<Object> construirMsgErro(Exception e, HttpStatus httpStatus, WebRequest request) {
        return construirMsgErro(e, e.getMessage(), httpStatus, request);
    }
}
