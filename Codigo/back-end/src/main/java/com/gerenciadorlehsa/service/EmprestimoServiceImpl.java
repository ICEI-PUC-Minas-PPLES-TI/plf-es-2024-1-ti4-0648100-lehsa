package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.repository.EmprestimoRepository;
import com.gerenciadorlehsa.security.UsuarioDetails;
import com.gerenciadorlehsa.service.interfaces.EmprestimoService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.EMPRESTIMO_SERVICE;

@Slf4j(topic = EMPRESTIMO_SERVICE)
@Service
public class EmprestimoServiceImpl extends TransacaoService<Emprestimo> implements OperacoesCRUDService<Emprestimo>, EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;

    @Autowired
    public EmprestimoServiceImpl (ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService, EmprestimoRepository emprestimoRepository) {
        super (validadorAutorizacaoRequisicaoService);
        this.emprestimoRepository = emprestimoRepository;
    }

    @Override
    public Emprestimo encontrarPorId (UUID id) {
        return null;
    }

    @Override
    public Emprestimo criar (Emprestimo obj) {
        return null;
    }

    @Override
    public Emprestimo atualizar (Emprestimo obj) {
        return null;
    }

    @Override
    public void deletar (UUID id) {

    }

    @Override
    public List<Emprestimo> listarTodos () {
        return null;
    }

    @Override
    public void atualizarStatus (String status, UUID id) {

    }

    @Override
    public void verificarConflitoComTransacoesAprovadasOuConfirmadas (LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {

    }

    @Override
    public void verificarLimiteTransacaoEmAnalise (User participante) {

    }

    @Override
    public boolean ehSolicitante (Emprestimo transacao, UsuarioDetails usuarioDetails) {
        return false;
    }

    @Override
    public boolean ehUsuarioAutorizado (Emprestimo transacao, UsuarioDetails usuarioLogado) {
        return false;
    }

    @Override
    public void verificarTransacaoDeMesmaDataDoUsuario (User solicitante, Emprestimo transacao) {

    }
}
