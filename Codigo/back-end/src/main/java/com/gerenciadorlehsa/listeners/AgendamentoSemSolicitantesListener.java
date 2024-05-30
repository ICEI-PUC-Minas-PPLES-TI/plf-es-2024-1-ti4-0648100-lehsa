package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.events.AgendamentoSemSolicitantesEvent;
import com.gerenciadorlehsa.service.interfaces.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@AllArgsConstructor
public class AgendamentoSemSolicitantesListener {

    private final AgendamentoService agendamentoService;

    @EventListener
    public void handleAgendamentoSemSolicitantesEvent(AgendamentoSemSolicitantesEvent event) {
        agendamentoService.deletarAgendamentoSeVazio (event.getAgendamento ().getId ());
    }

}
