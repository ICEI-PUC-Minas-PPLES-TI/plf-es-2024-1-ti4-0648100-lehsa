package com.gerenciadorlehsa.service.interfaces;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Professor;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.security.UsuarioDetails;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface AgendamentoService {

    void atualizarTecnico (String email, @NotNull UUID id);

    void deletarAgendamentoSeVazio(UUID id);

    List<Object[]> listarDatasOcupadas();

    Agendamento professorConfirmaAgendamento(UUID id);

    void enviarEmailParaProfessor(Agendamento agendamento);

    void verificarTransacaoDeMesmaDataDoProfessor(Professor professor, Agendamento agendamento);

    boolean ehTecnico(Agendamento agendamento, UsuarioDetails usuarioLogado);

    void checkTecnicoNaoSolicita(Agendamento agendamento);

    void verificarPerfilTecnico(User tecnico);

    void verificarConfirmacaoCadastroProfessor(Agendamento agendamento);

    Agendamento verificarNovoProfessor(Agendamento novoAgedamento, Agendamento velhoAgendamento);

}
