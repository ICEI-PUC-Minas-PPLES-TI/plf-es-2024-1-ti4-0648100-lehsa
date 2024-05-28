package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UsuarioEvent extends ApplicationEvent {

    private String email;
    private User user;
    private EventType eventType;

    public enum EventType {
        ENCONTRAR_POR_EMAIL,

        USUARIO_REMOVIDO,
    }


    public UsuarioEvent(Object source, String email, EventType eventType) {
        super(source);
        this.email = email;
        this.eventType = eventType;
    }


    public UsuarioEvent(Object source, User user, EventType eventType) {
        super(source);
        this.user = user;
        this.eventType = eventType;
    }






}
