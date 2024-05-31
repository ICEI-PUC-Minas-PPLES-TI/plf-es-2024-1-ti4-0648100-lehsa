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
        private final User user;
        private final Agendamento agendamento;

        public AgendamentoSemSolicitantesEvent(Object source, User user, Agendamento agendamento) {
            super(source);
            this.user = user;
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
