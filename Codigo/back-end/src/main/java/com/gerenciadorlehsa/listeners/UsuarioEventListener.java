package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.events.AgendamentoEvents;
import com.gerenciadorlehsa.events.UsuarioEvents;
import com.gerenciadorlehsa.service.interfaces.AgendamentoService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class UsuarioEventListener {

    private final AgendamentoService agendamentoService;
    private final OperacoesCRUDService<Agendamento> operacoesCRUDService;

    @EventListener
    public void handleAgendamentoSemSolicitantesEvent(UsuarioEvents.AgendamentoSemSolicitantesEvent event) {
        agendamentoService.deletarAgendamentoSeVazio(event.getAgendamento().getId());
    }

    @EventListener
    public void handleAgendamentoSemTecnicoEvent(UsuarioEvents.AgendamentoSemTecnicoEvent event) {
        UUID agendamentoId = event.getAgendamento().getId();
        if (agendamentoService.agendamentoExiste(agendamentoId)) {
            Agendamento agendamentoAtt = operacoesCRUDService.encontrarPorId(agendamentoId);
            agendamentoAtt.setTecnico(null);
            agendamentoService.saveAgendamento(agendamentoAtt);
        }
    }
}
