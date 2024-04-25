package com.gerenciadorlehsa.listener;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.User;
import jakarta.persistence.PostPersist;

public class AgendamentoListener {
    @PostPersist
    public void aposPersistirAgendamento(Agendamento agendamento) {

        User tecnico = agendamento.getTecnico();
        if (tecnico != null) {
            tecnico.getAgendamentosComoTecnico().add(agendamento);
        }

        for (User solicitante : agendamento.getSolicitantes()) {
            solicitante.getAgendamentosRealizados().add(agendamento);
        }

        for (Item item : agendamento.getItens()) {
            item.getAgendamentos().add(agendamento);
        }
    }
}
