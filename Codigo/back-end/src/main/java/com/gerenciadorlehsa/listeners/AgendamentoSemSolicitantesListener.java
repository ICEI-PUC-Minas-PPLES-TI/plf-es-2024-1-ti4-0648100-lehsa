package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.events.AgendamentoSemSolicitantesEvent;
import com.gerenciadorlehsa.service.interfaces.AgendamentoService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AgendamentoSemSolicitantesListener {

    private final AgendamentoService agendamentoService;

    @EventListener
    public void handleAgendamentoSemSolicitantesEvent(AgendamentoSemSolicitantesEvent event) {
        agendamentoService.deletarAgendamentoSeVazio (event.getAgendamento ().getId ());
    }

}
