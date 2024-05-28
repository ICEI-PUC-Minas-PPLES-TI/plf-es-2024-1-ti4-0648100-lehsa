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
    @Operation(
            summary = "Lidando com evento de agendamento sem solicitantes",
            description = "Este método escuta evento AgendamentoSemSolicitantesEvent, remove o usuário de todos os " +
                    "seus " +
                    "agendamentos e exclui qualquer agendamento que não tenha mais participante"
    )
    public void handleAgendamentoSemSolicitantesEvent(AgendamentoSemSolicitantesEvent event) {
        User user = event.getUser();
        List<Agendamento> agendamentos = user.getAgendamentosRealizados();

        agendamentos.forEach(agendamento -> {

            agendamento.getSolicitantes().remove(user);

            if (agendamento.getSolicitantes().isEmpty()) {
                agendamentoService.deletarAgendamentoSeVazio(agendamento.getId());
            }
        });
    }

}
