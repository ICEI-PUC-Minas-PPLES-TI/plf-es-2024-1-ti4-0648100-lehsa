package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.service.interfaces.AgendamentoService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AgendamentoUsuarioEvent {


    private final AgendamentoService agendamentoService;

    @EventListener
    public void handleRelacaoAtualizada(com.gerenciadorlehsa.events.AgendamentoUsuarioEvent event) {
        User user = event.getUser();
        List<Agendamento> agendamentos = user.getAgendamentosRealizados();

        if (agendamentos != null && !agendamentos.isEmpty()) {
            for (Agendamento agendamento : agendamentos) {
                agendamento.getSolicitantes().remove(user);

                if (agendamento.getSolicitantes().isEmpty()) {
                    agendamentoService.deletarAgendamentoSeVazio(agendamento.getId());
                }
            }
        }
    }
}
