package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {

    List<Endereco> findByCep(String cep);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Endereco e " +
            "WHERE e.cep = :#{#endereco.cep} " +
            "AND e.rua = :#{#endereco.rua} " +
            "AND e.numero = :#{#endereco.numero} " +
            "AND e.bairro = :#{#endereco.bairro} " +
            "AND e.cidade = :#{#endereco.cidade} " +
            "AND e.uf = :#{#endereco.uf} " +
            "AND e.complemento = :#{#endereco.complemento}"
    )
    boolean existsByEndereco(@Param("endereco") Endereco endereco);
}
