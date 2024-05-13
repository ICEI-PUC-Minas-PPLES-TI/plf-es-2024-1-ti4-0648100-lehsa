package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    @Query("SELECT a FROM Agendamento a WHERE a.statusTransacaoItem = 'APROVADO' OR a.statusTransacaoItem = 'CONFIRMADO' AND " +
            "((a.dataHoraInicio <= :novaDataHoraFim AND a.dataHoraFim >= :novaDataHoraInicio) OR " +
            "(a.dataHoraInicio >= :novaDataHoraInicio AND a.dataHoraInicio <= :novaDataHoraFim) OR " +
            "(a.dataHoraFim >= :novaDataHoraInicio AND a.dataHoraFim <= :novaDataHoraFim))")
    List<Agendamento> findAprovadosOuConfirmadosConflitantes(LocalDateTime novaDataHoraInicio, LocalDateTime novaDataHoraFim);

    @Query("SELECT a FROM Agendamento a WHERE (a.statusTransacaoItem = 'APROVADO' OR a.statusTransacaoItem = 'COMPROVADO') " +
            "AND ((a.dataHoraInicio <= :dataFim AND a.dataHoraFim >= :dataInicio) OR " +
            "(a.dataHoraInicio <= :dataInicio AND a.dataHoraFim >= :dataInicio))")
    List<Agendamento> buscarConflitosDeAgendamento(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);


    List<Agendamento> findBySolicitantes(User usuario);
}
