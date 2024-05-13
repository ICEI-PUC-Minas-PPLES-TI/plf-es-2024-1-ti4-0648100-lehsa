package com.gerenciadorlehsa.service.components;


import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Transacao;
import com.gerenciadorlehsa.exceptions.lancaveis.AgendamentoException;
import com.gerenciadorlehsa.exceptions.lancaveis.TransacaoException;
import com.gerenciadorlehsa.service.TransacaoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class ValidadorTransacaoComp {

    private final TransacaoService<Emprestimo> transacaoEmprestimoService;
    private final TransacaoService<Agendamento> transacaoAgendamentoService;

    public void validarTransacao(Agendamento transacao) {
        verificarItensNoMapa(transacao);
        verificarQuantidadeDeItemInValida(transacao.getItensQuantidade ());
        verificarQuantidadeSelecionada(transacao);
    }


    public void verificarQuantidadeSelecionada(Agendamento transacao) {

        for (Map.Entry<Item, Integer> entry : transacao.getItensQuantidade().entrySet()) {
            Item item = entry.getKey();
            int quantidadeSelecionada = entry.getValue();

            int quantidadeDisponivel = calcularQuantidadeDisponivelParaItem(item, transacao.getDataHoraInicio(), transacao.getDataHoraFim());

            if (quantidadeSelecionada > quantidadeDisponivel) {
                throw new TransacaoException ("Quantidade selecionada para o item " + item.getNome() + " excede a quantidade " +
                        "disponível.");
            }
        }
    }


    public int calcularQuantidadeDisponivelParaItem(Item item, LocalDateTime inicio, LocalDateTime fim) {
        List<Emprestimo> emprestimosEmConflito = buscarEmprestimosEmConflito(inicio, fim);
        List<Agendamento> agendamentosEmConflito = buscarAgendamentosEmConflito(inicio, fim);

        int quantidadeEmprestada = transacaoEmprestimoService.calcularQuantidadeTransacao (item, emprestimosEmConflito);
        int quantidadeAgendada = transacaoAgendamentoService.calcularQuantidadeTransacao (item, agendamentosEmConflito);

        int quantidadeTotal = item.getQuantidade();
        int quantidadeDisponivel = quantidadeTotal - quantidadeEmprestada - quantidadeAgendada;
        return Math.max(quantidadeDisponivel, 0);
    }


    private List<Emprestimo> buscarEmprestimosEmConflito(LocalDateTime inicio, LocalDateTime fim) {
        return transacaoEmprestimoService.transacoesAprovadasOuConfirmadasConflitantes (inicio, fim);
    }

    private List<Agendamento> buscarAgendamentosEmConflito(LocalDateTime inicio, LocalDateTime fim) {
        return transacaoAgendamentoService.transacoesAprovadasOuConfirmadasConflitantes (inicio,fim);
    }

    private void verificarQuantidadeDeItemInValida(Map<Item, Integer> itensQuantidade) {

        for (Map.Entry<Item, Integer> entrada : itensQuantidade.entrySet()) {
            Item item = entrada.getKey();
            Integer quantidadeInformada = entrada.getValue();

            if (quantidadeInformada < 0) {
                throw new AgendamentoException ("A quantidade informada é menor que zero");
            }

            if (quantidadeInformada > item.getQuantidade()) {
                throw new AgendamentoException ("A quantidade informada é maior do que a quantidade existe em estoque");
            }
        }

    }


    private void verificarItensNoMapa(Agendamento transacao) {

        List<Item> itens = transacao.getItens();
        Map<Item, Integer> itensQuantidade = transacao.getItensQuantidade();

        if (itens.size() != itensQuantidade.size()) {
            throw new AgendamentoException ("A quantidade de itens e quantidade de unidades de cada item diferem!");
        }

        for (Item item : itens) {
            if (!itensQuantidade.containsKey(item)) {
               throw new AgendamentoException ("O item " + item.getNome () + " não é chave do mapa de itens " +
                       "quantidade");
            }
        }
    }



}
