package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class AgendamentoSemSolicitantesEvent extends ApplicationEvent {

    private User user;

    public AgendamentoSemSolicitantesEvent (Object source, User user) {
        super (source);
        this.user = user;
    }

}
