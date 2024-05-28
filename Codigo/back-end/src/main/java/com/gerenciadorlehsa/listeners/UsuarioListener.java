package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.events.UsuarioEvent;
import com.gerenciadorlehsa.service.interfaces.AgendamentoService;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UsuarioListener {

    private final UsuarioService usuarioService;
    private final AgendamentoService agendamentoService;

    @EventListener
    public void handleEncontrarPorEmailEvent(UsuarioEvent event) {
                User user = usuarioService.encontrarPorEmail(event.getEmail());
                event.setUser(user);
    }


    @EventListener
    @Operation(
            summary = "Lidando com evento de remoção do usuário",
            description = "Este método escuta evento AgendamentoUsuarioEvent, remove o usuário de todos os seus " +
                    "agendamentos e exclui qualquer agendamento que não tenha mais participante"
    )
    public void handleUsuarioRemovidoEvent(UsuarioEvent event) {
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
