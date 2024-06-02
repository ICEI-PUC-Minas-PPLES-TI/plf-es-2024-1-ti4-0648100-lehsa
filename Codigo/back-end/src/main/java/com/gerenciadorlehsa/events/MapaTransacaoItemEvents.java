package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.UUID;

public class MapaTransacaoItemEvents {



    @Getter
    @Setter
    public static class RemoveMatchingIdEvent extends ApplicationEvent {
        private final List<Agendamento> agendamentos;
        private final List<Emprestimo> emprestimos;
        private final UUID id;

        public RemoveMatchingIdEvent(Object source,List<Agendamento> agendamentos, List<Emprestimo> emprestimos,UUID id) {
            super(source);
            this.agendamentos = agendamentos;
            this.emprestimos = emprestimos;
            this.id = id;
        }
    }

}
