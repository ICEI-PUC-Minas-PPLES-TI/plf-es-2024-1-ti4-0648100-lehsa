package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Transacao;
import com.gerenciadorlehsa.exceptions.lancaveis.AgendamentoException;
import com.gerenciadorlehsa.exceptions.lancaveis.TransacaoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.MAPA_TRANSACAO_ITEM_SERVICE;

@Slf4j(topic = MAPA_TRANSACAO_ITEM_SERVICE)
@Service
@AllArgsConstructor
public class MapaTransacaoItemService<T extends Transacao> {

    private final TransacaoService<Emprestimo> transacaoEmprestimoService;
    private final TransacaoService<Agendamento> transacaoAgendamentoService;

    public void validarMapa(T transacao) {
        log.info (">>> Validando Mapa da relação entre Transacao e Item");
        verificarQuantidadeDeItemInValida(transacao.getItensQuantidade());
        verificarQuantidadeSelecionada(transacao);
    }

    public void verificarQuantidadeSelecionada(T transacao) {
        log.info (">>> Verificando disponibilidade do número de itens");
        for (Map.Entry<Item, Integer> entry : transacao.getItensQuantidade().entrySet()) {
            Item item = entry.getKey();
            int quantidadeSelecionada = entry.getValue();
            int quantidadeDisponivel = calcularQuantidadeDisponivelParaItem(item, transacao.getDataHoraInicio(), transacao.getDataHoraFim());

            if (quantidadeSelecionada > quantidadeDisponivel) {
                throw new TransacaoException ("Quantidade selecionada para o item " + item.getNome() + " excede a quantidade disponível.");
            }
        }
    }

    public int calcularQuantidadeDisponivelParaItem(Item item, LocalDateTime inicio, LocalDateTime fim) {
        List<Emprestimo> emprestimosEmConflito = buscarEmprestimosEmConflito(inicio, fim);
        List<Agendamento> agendamentosEmConflito = buscarAgendamentosEmConflito(inicio, fim);

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

    private void verificarQuantidadeDeItemInValida(Map<Item, Integer> itensQuantidade) {
        log.info (">>> Validando a quantidade de item da transação");
        for (Map.Entry<Item, Integer> entrada : itensQuantidade.entrySet()) {
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

    public void deletarItensAssociados(Item item) {
        transacaoAgendamentoService.deletarItensAssociados (item);
        transacaoEmprestimoService.deletarItensAssociados (item);
    }


}
