package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Item;
//import com.gerenciadorlehsa.entity.enums.TipoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    //List<Item> findByTipoItem(TipoItem tipoItem);
}
