package com.gerenciadorlehsa.service.interfaces;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.User;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface AgendamentoService {
    void atualizarTecnico (User tecnico, @NotNull UUID id);

    void deletarAgendamentoSeVazio(UUID id);

    List<Object[]> listarDatasOcupadas();

    Agendamento professorConfirmaAgendamento(UUID id);

    void enviarEmailParaProfessor(Agendamento agendamento);

    void verificarConfirmacaoCadastroProfessor(Agendamento agendamento);

}
