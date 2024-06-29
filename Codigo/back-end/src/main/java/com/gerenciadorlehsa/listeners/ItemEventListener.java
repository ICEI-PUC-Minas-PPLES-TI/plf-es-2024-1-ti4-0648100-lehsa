package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.events.ItemEvents;
import com.gerenciadorlehsa.repository.AgendamentoRepository;
import com.gerenciadorlehsa.repository.EmprestimoRepository;
import com.gerenciadorlehsa.service.TransacaoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.ITEM_EVENT_LISTENER;

@Slf4j(topic = ITEM_EVENT_LISTENER)
@Component
@AllArgsConstructor
public class ItemEventListener {

    private final TransacaoService<Emprestimo, EmprestimoRepository> transacaoEmprestimoService;
    private final TransacaoService<Agendamento, AgendamentoRepository> transacaoAgendamentoService;

    @EventListener
    public void handleDeletarItensAssociadosEvent(ItemEvents.DeletarItemEmTransacoesEvent event) {
        log.info (">>> Lidando com evento de deletar a chave item do mapa da associação com uma transação");

        Item item = event.getItem ();
        transacaoAgendamentoService.deletarItemAssociado (item);
        transacaoEmprestimoService.deletarItemAssociado (item);
    }
}
