package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.events.ItemEvents;
import com.gerenciadorlehsa.repository.AgendamentoRepository;
import com.gerenciadorlehsa.repository.EmprestimoRepository;
import com.gerenciadorlehsa.service.TransacaoService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ItemEventListener {

    private final TransacaoService<Emprestimo, EmprestimoRepository> transacaoEmprestimoService;
    private final TransacaoService<Agendamento, AgendamentoRepository> transacaoAgendamentoService;

    @EventListener
    public void handleDeletarItensAssociadosEvent(ItemEvents.DeletarItemEmTransacoesEvent event) {
        Item item = event.getItem ();
        transacaoAgendamentoService.deletarItemAssociado (item);
        transacaoEmprestimoService.deletarItemAssociado (item);
    }
}
