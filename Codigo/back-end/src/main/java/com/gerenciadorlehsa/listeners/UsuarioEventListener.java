package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.events.UsuarioEvents;
import com.gerenciadorlehsa.service.interfaces.AgendamentoService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.UUID;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.USUARIO_EVENT_LISTENER;

@Slf4j(topic = USUARIO_EVENT_LISTENER)
@Component
@AllArgsConstructor
public class UsuarioEventListener {

    private final AgendamentoService agendamentoService;
    private final OperacoesCRUDService<Agendamento> operacoesCRUDService;

    @EventListener
    public void handleAgendamentoSemSolicitantesEvent(UsuarioEvents.AgendamentoSemSolicitantesEvent event) {
        log.info (">>> Lidando com evento de deletar um agendamento cuja lista de solicitantes está vazia");

        agendamentoService.deletarAgendamentoSeVazio(event.getAgendamento().getId());
    }

    @EventListener
    public void handleAgendamentoSemTecnicoEvent(UsuarioEvents.AgendamentoSemTecnicoEvent event) {
        log.info (">>> Lidando com evento de agendamento ficar sem técnico");

        UUID agendamentoId = event.getAgendamento().getId();
        if (agendamentoService.agendamentoExiste(agendamentoId)) {
            Agendamento agendamentoAtt = operacoesCRUDService.encontrarPorId(agendamentoId);
            agendamentoAtt.setTecnico(null);
            agendamentoService.saveAgendamento(agendamentoAtt);
        }
    }
}
