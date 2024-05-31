package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.events.AgendamentoEvents;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AgendamentoEventListener {

    private final UsuarioService usuarioService;
    @EventListener
    public void handleObterTecnicoPorEmailEvent(AgendamentoEvents.ObterTecnicoPorEmailEvent event) {
        User user = usuarioService.encontrarPorEmail(event.getEmail());
        event.setUser(user);
    }

}
