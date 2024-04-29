package com.gerenciadorlehsa.service.interfaces;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface AgendamentoService {
    void atualizarStatus (@NotNull String status, @NotNull UUID id);
}
