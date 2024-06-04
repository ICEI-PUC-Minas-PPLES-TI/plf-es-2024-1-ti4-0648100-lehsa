package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public interface AgendamentoRepository extends TransacaoRepository<Agendamento> {

    @Query("SELECT a.dataHoraInicio, a.dataHoraFim FROM Agendamento a " +
            "WHERE (a.statusTransacaoItem = 'APROVADO' OR a.statusTransacaoItem = 'CONFIRMADO') " +
            "AND a.dataHoraInicio >= CURRENT_TIMESTAMP ORDER BY a.dataHoraInicio")
    List<Object[]> findDataHoraInicioAndFim();
}
