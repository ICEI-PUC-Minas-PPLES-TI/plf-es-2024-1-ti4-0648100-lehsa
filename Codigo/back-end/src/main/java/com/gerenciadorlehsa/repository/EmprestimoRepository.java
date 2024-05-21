package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Endereco;
import com.gerenciadorlehsa.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {

    @Query("SELECT e FROM Emprestimo e WHERE e.statusTransacaoItem = 'APROVADO' OR e.statusTransacaoItem = 'CONFIRMADO' AND " +
            "((e.dataHoraInicio <= :novaDataHoraFim AND e.dataHoraFim >= :novaDataHoraInicio) OR " +
            "(e.dataHoraInicio >= :novaDataHoraInicio AND e.dataHoraInicio <= :novaDataHoraFim) OR " +
            "(e.dataHoraFim >= :novaDataHoraInicio AND e.dataHoraFim <= :novaDataHoraFim))")
    List<Emprestimo> findAprovadosOuConfirmadosConflitantes(LocalDateTime novaDataHoraInicio, LocalDateTime novaDataHoraFim);

    int countEmprestimoByLocalUso(Endereco localUso);

    default List<Emprestimo> findByItem(Item item) {
        List<Emprestimo> allEmprestimos = findAll();
        List<Emprestimo> emprestimosComItem = new ArrayList<>();
        for (Emprestimo emprestimo : allEmprestimos) {
            if (emprestimo.getItensQuantidade().containsKey(item)) {
                emprestimosComItem.add(emprestimo);
            }
        }
        return emprestimosComItem;
    }
}
