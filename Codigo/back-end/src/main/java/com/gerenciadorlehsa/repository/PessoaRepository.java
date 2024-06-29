package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface PessoaRepository<T extends Pessoa> extends JpaRepository<T, UUID> {

    @Transactional(readOnly = true)
    Optional<T> findByEmail(String email);

}
