package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

}
