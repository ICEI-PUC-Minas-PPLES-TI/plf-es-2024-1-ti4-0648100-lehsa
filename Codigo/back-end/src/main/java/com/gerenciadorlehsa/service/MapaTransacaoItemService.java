package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Transacao;
import com.gerenciadorlehsa.exceptions.lancaveis.AgendamentoException;
import com.gerenciadorlehsa.exceptions.lancaveis.TransacaoException;
import com.gerenciadorlehsa.repository.AgendamentoRepository;
import com.gerenciadorlehsa.repository.EmprestimoRepository;
import com.gerenciadorlehsa.service.interfaces.EventPublisher;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.MAPA_TRANSACAO_ITEM_SERVICE;

@Slf4j(topic = MAPA_TRANSACAO_ITEM_SERVICE)
@Service
@AllArgsConstructor
@Schema(description = "Responsável por lidar com a relação entre item e transação")
public class MapaTransacaoItemService<T extends Transacao>{

    private final TransacaoService<Emprestimo, EmprestimoRepository> transacaoEmprestimoService;
    private final TransacaoService<Agendamento, AgendamentoRepository> transacaoAgendamentoService;

    public void validarMapa(T transacao) {
        log.info(">>> Validando Mapa para criação de Transacao");
        verificarQuantidadeDeItemInValida(transacao.getItensQuantidade());
        verificarQuantidadeSelecionada(null, transacao);
    }

    public void validarMapa(UUID id, T transacao) {
        log.info(">>> Validando Mapa para atualização de Transacao");
        verificarQuantidadeDeItemInValida(transacao.getItensQuantidade());
        verificarQuantidadeSelecionada(id, transacao);
    }

    public void verificarQuantidadeSelecionada(UUID id, T transacao) {
        log.info (">>> Verificando disponibilidade do número de itens");
        for (Map.Entry<Item, Integer> entry : transacao.getItensQuantidade().entrySet()) {
            Item item = entry.getKey();
            int quantidadeSelecionada = entry.getValue();
            int quantidadeDisponivel = calcularQuantidadeDisponivelParaItem(transacao, id, item);

            if (quantidadeSelecionada > quantidadeDisponivel) {
                throw new TransacaoException ("Quantidade selecionada para o item " + item.getNome() + " excede a quantidade disponível.");
            }
        }
    }

    public int calcularQuantidadeDisponivelParaItem(T transacao, UUID id, Item item) {
        LocalDateTime inicio = transacao.getDataHoraInicio ();
        LocalDateTime fim = transacao.getDataHoraFim ();

        List<Emprestimo> emprestimosEmConflito = buscarEmprestimosEmConflito(inicio, fim);
        List<Agendamento> agendamentosEmConflito = buscarAgendamentosEmConflito(inicio, fim);

        if (id != null) {

            if(isSameType (transacao, emprestimosEmConflito))
                removeIfMatchingId (id, emprestimosEmConflito);
            else
                removeIfMatchingId (id, agendamentosEmConflito);
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


    public void removeIfMatchingId(UUID id, List<? extends Transacao> transacoes) {
        transacoes.removeIf(transacao -> transacao.getId().equals(id));
    }

    public boolean isSameType(Transacao o1, List<? extends Transacao> o2) {
        if (o1 == null || o2 == null || o2.isEmpty()) {
            return false;
        }
        Transacao firstElement = o2.get(0);
        return o1.getClass().equals(firstElement.getClass());
    }

}
