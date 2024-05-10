package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {

}
