package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {

    List<Endereco> findByCep(String cep);
}
