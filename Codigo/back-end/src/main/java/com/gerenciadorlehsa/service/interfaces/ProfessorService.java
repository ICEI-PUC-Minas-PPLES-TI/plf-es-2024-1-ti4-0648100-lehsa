package com.gerenciadorlehsa.service.interfaces;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Professor;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.UUID;

public interface ProfessorService extends PessoaService<Professor>{
    Professor confirmaEmail(UUID id, Boolean value);

    void enviarEmail(Professor professor, String linkConfirmacao);

    List<Agendamento> listarAgendamentos(UUID id);
}
