package com.gerenciadorlehsa.service.interfaces;

import com.gerenciadorlehsa.entity.Professor;

import java.util.UUID;

public interface ProfessorService {

    Professor confirmaEmail(UUID id, Boolean value);
}
