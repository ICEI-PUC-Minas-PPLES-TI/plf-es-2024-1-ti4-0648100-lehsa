package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<Professor, UUID> {

    @Transactional(readOnly = true)
    Optional<Professor> findByEmail(String email);
}
