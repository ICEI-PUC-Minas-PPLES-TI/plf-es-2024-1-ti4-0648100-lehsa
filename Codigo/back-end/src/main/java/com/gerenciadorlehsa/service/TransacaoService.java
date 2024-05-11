package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Transacao;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.security.UsuarioDetails;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.TRANSACAO_ITEM_SERVICE;


@Slf4j(topic = TRANSACAO_ITEM_SERVICE)
@Service
@Schema(description = "Superclasse abstrata que contém métodos e atributos em comum para qualquer tipo que é subtipo " +
        "de TransacaoItem")
public abstract class TransacaoService<T extends Transacao> {

    protected final ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;

    public TransacaoService (ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService) {
        this.validadorAutorizacaoRequisicaoService = validadorAutorizacaoRequisicaoService;
    }


    public abstract int calcularQuantidadeTransacao(Item item, List<T> transacao);

    public abstract void atualizarStatus (@NotNull String status, @NotNull UUID id);


    public abstract List<T> transacoesAprovadasOuConfirmadasConflitantes(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim);

    public abstract void verificarLimiteTransacaoEmAnalise(User participante);


    public abstract boolean ehSolicitante(T transacao, UsuarioDetails usuarioDetails);

    public abstract boolean ehUsuarioAutorizado(T transacao, UsuarioDetails usuarioLogado);


    public abstract void verificarTransacaoDeMesmaDataDoUsuario(User solicitante, T transacao);



    protected boolean temConflitoDeData(T transacaoExistente, T novaTransacao) {
        log.info(">>> Verificando datas conflitantes: barrando agendamento solicitado em uma mesma data");

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


    protected boolean temQuantidadeDeItemInValida(Map<Item, Integer> itensQuantidade) {

        for (Map.Entry<Item, Integer> entrada : itensQuantidade.entrySet()) {
            Item item = entrada.getKey();
            Integer quantidadeInformada = entrada.getValue();

            if (quantidadeInformada < 0) {
                return true;
            }

            if (quantidadeInformada > item.getQuantidade()) {
                return true;
            }
        }
        return false;
    }


    protected boolean temTodosOsItensNoMapa(T transacao) {

        List<Item> itens = transacao.getItens();
        Map<Item, Integer> itensQuantidade = transacao.getItensQuantidade();

        if (itens.size() != itensQuantidade.size()) {
            return false;
        }

        for (Item item : itens) {
            if (!itensQuantidade.containsKey(item)) {
                return false;
            }
        }
        return true;
    }


}
