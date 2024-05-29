package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.Agendamento;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class AgendamentoSemTecnicoEvent extends ApplicationEvent {

    private Agendamento agendamento;

    public AgendamentoSemTecnicoEvent (Object source, Agendamento agendamento) {
        super (source);
        this.agendamento = agendamento;
    }
}
