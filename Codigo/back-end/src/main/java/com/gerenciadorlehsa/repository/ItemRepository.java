package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.enums.TipoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    List<Item> findByTipoItem(TipoItem tipoItem);

    @Query(value = "SELECT i from Item i WHERE i.nome LIKE :nome%")
    List<Item> findByNome(@Param("nome") String nome);

  /*  @Modifying
    @Query(value = "DELETE FROM AGENDAMENTO_ITEM_QUANTIDADE WHERE ITEM_ID = :itemId", nativeQuery = true)
    void deleteByItemId(@Param("itemId") UUID itemId);*/

}
