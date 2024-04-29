package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    @Query("SELECT a FROM Agendamento a WHERE a.statusTransacaoItem = 'APROVADO' OR a.statusTransacaoItem = 'CONFIRMADO' AND " +
            "((a.dataHoraInicio <= :novaDataHoraFim AND a.dataHoraFim >= :novaDataHoraInicio) OR " +
            "(a.dataHoraInicio >= :novaDataHoraInicio AND a.dataHoraInicio <= :novaDataHoraFim) OR " +
            "(a.dataHoraFim >= :novaDataHoraInicio AND a.dataHoraFim <= :novaDataHoraFim))")
    List<Agendamento> findAprovadosOuConfirmadosConflitantes(LocalDateTime novaDataHoraInicio, LocalDateTime novaDataHoraFim);

}
