package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Transacao;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.entity.enums.StatusTransacao;
import com.gerenciadorlehsa.exceptions.lancaveis.AtualizarStatusException;
import com.gerenciadorlehsa.exceptions.lancaveis.EnumNaoEncontradoException;
import com.gerenciadorlehsa.exceptions.lancaveis.TempoExpiradoException;
import com.gerenciadorlehsa.exceptions.lancaveis.UsuarioNaoAutorizadoException;
import com.gerenciadorlehsa.repository.TransacaoRepository;
import com.gerenciadorlehsa.security.UserDetailsImpl;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import com.gerenciadorlehsa.util.DataHoraUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import static com.gerenciadorlehsa.entity.enums.StatusTransacao.*;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.TRANSACAO_SERVICE;

@Slf4j(topic = TRANSACAO_SERVICE)
@Service
@Schema(description = "Superclasse abstrata que contém métodos e atributos em comum para qualquer tipo que é subtipo " +
        "de TransacaoItem")
public abstract class TransacaoService<T extends Transacao,  R extends TransacaoRepository<T>> {

    protected final ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;


    @Autowired
    public TransacaoService(ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService) {
        this.validadorAutorizacaoRequisicaoService = validadorAutorizacaoRequisicaoService;
    }

    protected abstract R getTransacaoRepository();


    public abstract boolean ehSolicitante(T transacao, UserDetailsImpl userDetailsImpl);

    public abstract boolean ehUsuarioAutorizado(T transacao, UserDetailsImpl usuarioLogado);


    public abstract void verificarTransacaoDeMesmaDataDoUsuario(User solicitante, T transacao);


    public abstract void verificarLimiteTransacaoEmAnalise(User participante);


    public abstract void atualizarStatus (@NotNull String status, @NotNull UUID id);


    public int calcularQuantidadeTransacao(Item item, List<T> transacoes) {
        int quantidadeEmprestada = 0;
        for (T transacao : transacoes) {
            Integer quantidade = transacao.getItensQuantidade().getOrDefault(item, 0);
            quantidadeEmprestada += quantidade;
        }
        return quantidadeEmprestada;
    }


    public List<T> transacoesAprovadasOuConfirmadasConflitantes(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        return getTransacaoRepository ().findAprovadosOuConfirmadosConflitantes (dataHoraInicio, dataHoraFim);
    }


    public void deletarItemAssociado(Item item) {
        List<T> transacoes = getTransacaoRepository().findByItem(item);

        if (transacoes != null && !transacoes.isEmpty()) {
            for (T transacao : transacoes) {
                transacao.getItensQuantidade().remove(item);
                getTransacaoRepository().save(transacao);
            }
        }
    }


    public void verificarCondicoesDeConfirmacao(T transacao, StatusTransacao statusUpperCase) {
        if (statusUpperCase == StatusTransacao.CONFIRMADO) {
            if (!transacao.getStatusTransacao ().equals (StatusTransacao.APROVADO)) {
                throw new AtualizarStatusException ("Para confirmar a transação, ela precisa estar aprovada.");
            }

            if (DataHoraUtil.tempoTransacaoExpirado (transacao.getDataHoraInicio ())) {
                transacao.setStatusTransacao (StatusTransacao.NAO_COMPARECEU);
                getTransacaoRepository ().save (transacao);
                throw new TempoExpiradoException ("A confirmação deve ser feita até 24 horas antes da data e hora de início da transação.");
            }
        }
    }


    public void verificarConflitosDeTransacaoAPROVADOeCONFIRMADO(T transacao, StatusTransacao status) {
        R transacaoRepository = getTransacaoRepository();
        LocalDateTime inicio = transacao.getDataHoraInicio();
        LocalDateTime fim = transacao.getDataHoraFim();

        List<T> conflitos = transacaoRepository.findAprovadosOuConfirmadosConflitantes(inicio, fim);

        if (!conflitos.isEmpty() && (status == StatusTransacao.APROVADO || status == StatusTransacao.CONFIRMADO)) {
            throw new AtualizarStatusException("Uma transação para essa data já foi aprovada ou confirmada.");
        }
    }



    public void verificarCondicoesDeAprovacao(T transacao, StatusTransacao statusUpperCase) {
        if (statusUpperCase == APROVADO) {
            if(!transacao.getStatusTransacao ().equals (EM_ANALISE))
                throw new AtualizarStatusException ("O transação precisa estar EM_ANALISE para ser APROVADO");
        }
    }

    public boolean temConflitoDeData(T transacaoExistente, T novaTransacao) {
        log.info(">>> Verificando datas conflitantes: barrando transacao solicitado em uma mesma data");

        if (transacaoExistente.getId() == novaTransacao.getId())
            return false;

        LocalDateTime dataHoraInicioExistente = transacaoExistente.getDataHoraInicio();
        LocalDateTime dataHoraFimExistente = transacaoExistente.getDataHoraFim();
        LocalDateTime dataHoraInicioNovo = novaTransacao.getDataHoraInicio();
        LocalDateTime dataHoraFimNovo = novaTransacao.getDataHoraFim();

        return (dataHoraInicioNovo.isBefore(dataHoraFimExistente) ||
                dataHoraInicioNovo.isEqual(dataHoraFimExistente)) &&
                (dataHoraFimNovo.isAfter(dataHoraInicioExistente) ||
                        dataHoraFimNovo.isEqual(dataHoraInicioExistente));
    }

    public StatusTransacao getStatusUpperCase(String status) {
        try {
            return Enum.valueOf(StatusTransacao.class, status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new EnumNaoEncontradoException ("O status passado não existe: " + status);
        }
    }

    public void verificarAutorizacaoDoUsuario(T transacao, StatusTransacao status) {
        if (status.equals(CANCELADO) || status.equals(CONFIRMADO)) {
            UserDetailsImpl usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();
            if (!ehUsuarioAutorizado(transacao, usuarioLogado))
                throw new UsuarioNaoAutorizadoException ("O usuário não possui permissão para atualizar a transacão");
        } else
            validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
    }



    public void verificarAutorizacaoDoUsuario(T transacao) {
        UserDetailsImpl usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();
        if (!ehSolicitante(transacao, usuarioLogado)) {
            throw new UsuarioNaoAutorizadoException("O usuário não possui permissão para atualizar a transação");
        }
    }

    public List<String> encontrarAtributosIguais(T transacaoA, T transacaoB) {
        List<String> atributosIguais = new ArrayList<> ();

        if (transacaoA.getDataHoraInicio().isEqual(transacaoB.getDataHoraInicio()))
            atributosIguais.add("dataHoraInicio");
        if (transacaoA.getDataHoraFim().isEqual(transacaoB.getDataHoraFim()))
            atributosIguais.add("dataHoraFim");
        if (Objects.equals(transacaoA.getObservacaoSolicitacao(), transacaoB.getObservacaoSolicitacao()))
            atributosIguais.add("observacaoSolicitacao");

        return atributosIguais;
    }




}
