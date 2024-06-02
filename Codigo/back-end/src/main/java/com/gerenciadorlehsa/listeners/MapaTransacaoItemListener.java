package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Transacao;
import com.gerenciadorlehsa.events.MapaTransacaoItemEvents;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class MapaTransacaoItemListener {

    @EventListener
    public void handleRemoveMatchingId(MapaTransacaoItemEvents.RemoveMatchingIdEvent event) {
        removeIfMatchingId(event.getId (), event.getAgendamentos ());
        removeIfMatchingId(event.getId (), event.getEmprestimos ());
    }


    public <T extends Transacao>void removeIfMatchingId(UUID id, List<T> transacoes) {
        transacoes.removeIf(transacao -> transacao.getId().equals(id));
    }
}
