package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    @Query("SELECT a FROM Agendamento a WHERE (a.statusTransacaoItem = 'APROVADO' OR a.statusTransacaoItem = 'CONFIRMADO') AND " +
            "((a.dataHoraInicio <= :novaDataHoraFim AND a.dataHoraFim >= :novaDataHoraInicio) OR " +
            "(a.dataHoraInicio >= :novaDataHoraInicio AND a.dataHoraInicio <= :novaDataHoraFim) OR " +
            "(a.dataHoraFim >= :novaDataHoraInicio AND a.dataHoraFim <= :novaDataHoraFim))")
    List<Agendamento> findAprovadosOuConfirmadosConflitantes(LocalDateTime novaDataHoraInicio, LocalDateTime novaDataHoraFim);

    @Query("SELECT a FROM Agendamento a WHERE :itemKey MEMBER OF a.itensQuantidade")
    List<Agendamento> findByItemKey(@Param("itemKey") UUID itemKey);

    List<Agendamento> findBySolicitantes(User usuario);


    default List<Agendamento> findByItem(Item item) {
        List<Agendamento> allAgendamentos = findAll();
        List<Agendamento> agendamentosComItem = new ArrayList<> ();
        for (Agendamento agendamento : allAgendamentos) {
            if (agendamento.getItensQuantidade().containsKey(item)) {
                agendamentosComItem.add(agendamento);
            }
        }
        return agendamentosComItem;
    }
}
