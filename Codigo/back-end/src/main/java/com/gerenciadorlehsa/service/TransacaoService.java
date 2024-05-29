package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Transacao;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.entity.enums.StatusTransacaoItem;
import com.gerenciadorlehsa.exceptions.lancaveis.EnumNaoEncontradoException;
import com.gerenciadorlehsa.exceptions.lancaveis.UsuarioNaoAutorizadoException;
import com.gerenciadorlehsa.security.UsuarioDetails;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import com.gerenciadorlehsa.util.DataHoraUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import static com.gerenciadorlehsa.entity.enums.StatusTransacaoItem.*;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.TRANSACAO_SERVICE;

@Slf4j(topic = TRANSACAO_SERVICE)
@Service
@Schema(description = "Superclasse abstrata que contém métodos e atributos em comum para qualquer tipo que é subtipo " +
        "de TransacaoItem")
public abstract class TransacaoService<T extends Transacao> {

    protected final ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;

    @Autowired
    public TransacaoService(ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService) {
        this.validadorAutorizacaoRequisicaoService = validadorAutorizacaoRequisicaoService;
    }

    public abstract int calcularQuantidadeTransacao(Item item, List<T> transacao);

    public abstract void atualizarStatus (@NotNull String status, @NotNull UUID id);


    public abstract List<T> transacoesAprovadasOuConfirmadasConflitantes(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim);

    public abstract void verificarLimiteTransacaoEmAnalise(User participante);


    public abstract boolean ehSolicitante(T transacao, UsuarioDetails usuarioDetails);

    public abstract boolean ehUsuarioAutorizado(T transacao, UsuarioDetails usuarioLogado);


    public abstract void verificarTransacaoDeMesmaDataDoUsuario(User solicitante, T transacao);

    public abstract void deletarItensAssociados(Item item);

    public abstract void verificarConflitosDeTransacaoAPROVADOeCONFIRMADO(T transacao, StatusTransacaoItem status);


    public abstract void verificarCondicoesDeConfirmacao(T transacao, StatusTransacaoItem statusTransacaoItem);


    public abstract void verificarCondicoesDeAprovacao(T agendamento, StatusTransacaoItem statusUpperCase);

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

    public StatusTransacaoItem getStatusUpperCase(String status) {
        try {
            return Enum.valueOf(StatusTransacaoItem.class, status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new EnumNaoEncontradoException ("O status passado não existe: " + status);
        }
    }

    public void verificarAutorizacaoDoUsuario(T transacao, StatusTransacaoItem status) {
        if (status.equals(CANCELADO) || status.equals(CONFIRMADO)) {
            UsuarioDetails usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();
            if (!ehUsuarioAutorizado(transacao, usuarioLogado))
                throw new UsuarioNaoAutorizadoException ("O usuário não possui permissão para atualizar a transacão");
        } else
            validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
    }



    public boolean tempoExpirado(LocalDateTime dataHoraInicio) {
        long difTempo = DataHoraUtil.calcularDiferencaDeTempo(LocalDateTime.now(),
                    dataHoraInicio);
        return difTempo < 24;
    }


    public void verificarAutorizacaoDoUsuario(T transacao) {
        UsuarioDetails usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();
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
