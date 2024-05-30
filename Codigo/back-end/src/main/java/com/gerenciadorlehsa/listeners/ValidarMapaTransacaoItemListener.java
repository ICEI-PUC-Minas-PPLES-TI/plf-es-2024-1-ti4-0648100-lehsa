package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Transacao;
import com.gerenciadorlehsa.events.ValidarMapaTransacaoItemEvent;
import com.gerenciadorlehsa.exceptions.lancaveis.AgendamentoException;
import com.gerenciadorlehsa.exceptions.lancaveis.TransacaoException;
import com.gerenciadorlehsa.service.TransacaoService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ValidarMapaTransacaoItemListener<T extends Transacao> {

    private final TransacaoService<Emprestimo> transacaoEmprestimoService;
    private final TransacaoService<Agendamento> transacaoAgendamentoService;

    @EventListener
    @Order(1)
    public void verificarQuantidadeDeItemInValida(ValidarMapaTransacaoItemEvent<T> event){
        for (Map.Entry<Item, Integer> entrada : event.getTransacao ().getItensQuantidade ().entrySet()) {
            Item item = entrada.getKey();
            Integer quantidadeInformada = entrada.getValue();

            if (quantidadeInformada < 0) {
                throw new AgendamentoException ("A quantidade informada é menor que zero");
            }

            if (quantidadeInformada > item.getQuantidade()) {
                throw new AgendamentoException("A quantidade informada é maior do que a quantidade que existe em estoque");
            }
        }
    }

    @EventListener
    @Order(2)
    public void verificarQuantidadeSelecionada(ValidarMapaTransacaoItemEvent<T> event) {
        for (Map.Entry<Item, Integer> entry : event.getTransacao ().getItensQuantidade ().entrySet()) {
            Item item = entry.getKey();
            int quantidadeSelecionada = entry.getValue();
            int quantidadeDisponivel = calcularQuantidadeDisponivelParaItem(event.getTransacao (), event.getTransacaoId (), item);

            if (quantidadeSelecionada > quantidadeDisponivel) {
                throw new TransacaoException ("Quantidade selecionada para o item " + item.getNome() + " excede a quantidade disponível.");
            }
        }
    }


    private int calcularQuantidadeDisponivelParaItem(T transacao, UUID id, Item item) {
        LocalDateTime inicio = transacao.getDataHoraInicio ();
        LocalDateTime fim = transacao.getDataHoraFim ();

        List<Emprestimo> emprestimosEmConflito = buscarEmprestimosEmConflito(inicio, fim);
        List<Agendamento> agendamentosEmConflito = buscarAgendamentosEmConflito(inicio, fim);

        if (id != null) {
            transacaoEmprestimoService.removeIfMatchingId(id, emprestimosEmConflito);
            transacaoAgendamentoService.removeIfMatchingId(id, agendamentosEmConflito);
        }

        int quantidadeEmprestada = transacaoEmprestimoService.calcularQuantidadeTransacao(item, emprestimosEmConflito);
        int quantidadeAgendada = transacaoAgendamentoService.calcularQuantidadeTransacao(item, agendamentosEmConflito);

        int quantidadeTotal = item.getQuantidade();
        int quantidadeDisponivel = quantidadeTotal - quantidadeEmprestada - quantidadeAgendada;
        return Math.max(quantidadeDisponivel, 0);
    }

    private List<Emprestimo> buscarEmprestimosEmConflito(LocalDateTime inicio, LocalDateTime fim) {
        return transacaoEmprestimoService.transacoesAprovadasOuConfirmadasConflitantes(inicio, fim);
    }

    private List<Agendamento> buscarAgendamentosEmConflito(LocalDateTime inicio, LocalDateTime fim) {
        return transacaoAgendamentoService.transacoesAprovadasOuConfirmadasConflitantes(inicio, fim);
    }


}
