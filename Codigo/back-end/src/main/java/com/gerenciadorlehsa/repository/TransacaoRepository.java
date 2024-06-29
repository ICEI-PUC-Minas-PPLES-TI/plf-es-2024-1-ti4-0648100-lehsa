package com.gerenciadorlehsa.repository;


import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@NoRepositoryBean
public interface TransacaoRepository<T extends Transacao> extends JpaRepository<T, UUID> {

    @Query("SELECT t FROM #{#entityName} t WHERE (t.statusTransacao = 'APROVADO' OR t.statusTransacao = 'CONFIRMADO') AND " +
            "((t.dataHoraInicio <= :novaDataHoraFim AND t.dataHoraFim >= :novaDataHoraInicio) OR " +
            "(t.dataHoraInicio >= :novaDataHoraInicio AND t.dataHoraInicio <= :novaDataHoraFim) OR " +
            "(t.dataHoraFim >= :novaDataHoraInicio AND t.dataHoraFim <= :novaDataHoraFim))")
    List<T> findAprovadosOuConfirmadosConflitantes(LocalDateTime novaDataHoraInicio, LocalDateTime novaDataHoraFim);

    default List<T> findByItem(Item item) {
        List<T> alltransacoes = findAll();
        List<T> transacaoComItem = new ArrayList<> ();
        for (T transacao : alltransacoes) {
            if (transacao.getItensQuantidade().containsKey(item)) {
                transacaoComItem.add(transacao);
            }
        }
        return transacaoComItem;
    }

}
