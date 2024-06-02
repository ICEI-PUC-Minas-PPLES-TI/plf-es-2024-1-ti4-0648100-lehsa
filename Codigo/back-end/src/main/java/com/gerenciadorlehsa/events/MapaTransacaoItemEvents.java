package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class MapaTransacaoItemEvents {



    @Getter
    @Setter
    public static class removeMatchingId extends ApplicationEvent {
        private final List<Agendamento> agendamentos;
        private final List<Emprestimo> emprestimos;

        public removeMatchingId(Object source,List<Agendamento> agendamentos, List<Emprestimo> emprestimos) {
            super(source);
            this.agendamentos = agendamentos;
            this.emprestimos = emprestimos;
        }
    }

}
