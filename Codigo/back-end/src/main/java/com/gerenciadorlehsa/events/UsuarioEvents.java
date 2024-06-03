package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

public class UsuarioEvents {
    @Getter
    @Setter
    public static class AgendamentoSemSolicitantesEvent extends ApplicationEvent {

        private final Agendamento agendamento;

        public AgendamentoSemSolicitantesEvent(Object source, Agendamento agendamento) {
            super(source);
            this.agendamento = agendamento;
        }
    }

    @Getter
    @Setter
    public static class AgendamentoSemTecnicoEvent extends ApplicationEvent {
        private final Agendamento agendamento;
        public AgendamentoSemTecnicoEvent(Object source, Agendamento agendamento) {
            super(source);
            this.agendamento = agendamento;
        }
    }
}
