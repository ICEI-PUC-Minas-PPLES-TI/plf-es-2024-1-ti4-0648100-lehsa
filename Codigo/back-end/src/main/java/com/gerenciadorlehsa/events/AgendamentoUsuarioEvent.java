package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AgendamentoUsuarioEvent extends ApplicationEvent {
    private final User user;

    public AgendamentoUsuarioEvent (Object source, User user) {
        super(source);
        this.user = user;
    }

}
