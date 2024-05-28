package com.gerenciadorlehsa.listeners;

import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.events.UsuarioEvent;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UsuarioEventListener {
    private final UsuarioService usuarioService;

    @EventListener
    public void handleUsuarioEvent(UsuarioEvent event) {
        switch (event.getEventType()) {
            case ENCONTRAR_POR_EMAIL:
                User user = usuarioService.encontrarPorEmail(event.getEmail());
                event.setUser(user);
                break;
            default:
                throw new UnsupportedOperationException("Tipo de evento não suportado: " + event.getEventType());
        }
    }
}
