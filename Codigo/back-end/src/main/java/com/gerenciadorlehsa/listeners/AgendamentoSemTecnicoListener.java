package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.events.AgendamentoSemSolicitantesEvent;
import com.gerenciadorlehsa.events.AgendamentoSemTecnicoEvent;
import com.gerenciadorlehsa.service.interfaces.AgendamentoService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AgendamentoSemTecnicoListener {

    private AgendamentoService agendamentoService;
    private OperacoesCRUDService<Agendamento> operacoesCRUDService;

    @EventListener
    public void handleAgendamentoSemTecnicoEvent(AgendamentoSemTecnicoEvent event) {
        UUID AgendamentoId = event.getAgendamento ().getId ();
        if(agendamentoService.agendamentoExiste (AgendamentoId)) {
            Agendamento agendamentoAtt = operacoesCRUDService.encontrarPorId (AgendamentoId);
            agendamentoAtt.setTecnico (null);
            agendamentoService.saveAgendamento (agendamentoAtt);
        }
    }

}
