package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.events.AgendamentoEvents;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_EVENT_LISTENER;

@Slf4j(topic = AGENDAMENTO_EVENT_LISTENER)
@Component
@AllArgsConstructor
public class AgendamentoEventListener {

    private final UsuarioService usuarioService;
    @EventListener
    public void handleObterTecnicoPorEmailEvent(AgendamentoEvents.ObterTecnicoPorEmailEvent event) {
        log.info (">>> Lidando com evento de encontrar técnico por e-mail");

        User user = usuarioService.encontrarPorEmail(event.getEmail());
        event.setUser(user);
    }

}
