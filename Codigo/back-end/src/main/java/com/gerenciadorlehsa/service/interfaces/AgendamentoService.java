package com.gerenciadorlehsa.service.interfaces;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.User;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface AgendamentoService {
    List<Agendamento>  listarAgendamentoUsuario (@NotNull User usuario);
    void atualizarTecnico (User tecnico, @NotNull UUID id);

    void deletarAgendamentoSemSolicitantes(UUID id);

}
