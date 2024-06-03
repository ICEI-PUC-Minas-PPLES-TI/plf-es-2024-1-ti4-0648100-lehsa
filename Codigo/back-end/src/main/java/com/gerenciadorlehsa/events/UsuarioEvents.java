package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

public class UsuarioEvents {

    @Getter
    @Setter
    public static class AgendamentoEvent extends ApplicationEvent {
        private final Agendamento agendamento;

        public AgendamentoEvent(Object source, Agendamento agendamento) {
            super(source);
            this.agendamento = agendamento;
        }
    }

    public static class AgendamentoSemSolicitantesEvent extends AgendamentoEvent {
        public AgendamentoSemSolicitantesEvent(Object source, Agendamento agendamento) {
            super(source, agendamento);
        }
    }

    public static class AgendamentoSemTecnicoEvent extends AgendamentoEvent {
        public AgendamentoSemTecnicoEvent(Object source, Agendamento agendamento) {
            super(source, agendamento);
        }
    }
}
