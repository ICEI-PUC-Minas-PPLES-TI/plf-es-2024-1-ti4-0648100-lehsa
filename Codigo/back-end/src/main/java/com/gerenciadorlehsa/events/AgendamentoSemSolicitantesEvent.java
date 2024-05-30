package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class AgendamentoSemSolicitantesEvent extends ApplicationEvent {

    private User user;

    private Agendamento agendamento;

    public AgendamentoSemSolicitantesEvent (Object source, User user, Agendamento agendamento) {
        super (source);
        this.user = user;
        this.agendamento = agendamento;
    }

}
