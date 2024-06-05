package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfessorRepository extends PessoaRepository<Professor> {
}
