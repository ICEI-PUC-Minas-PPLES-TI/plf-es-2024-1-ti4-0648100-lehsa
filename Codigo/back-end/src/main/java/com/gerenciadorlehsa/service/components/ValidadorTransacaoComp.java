package com.gerenciadorlehsa.service.components;


import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Transacao;
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


    //chamar método no controller para evitar referência circular.
    public <T extends Transacao> void verificarQuantidadeSelecionada(T transacao) {

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
}
