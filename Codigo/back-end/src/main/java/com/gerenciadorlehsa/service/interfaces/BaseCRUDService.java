package com.gerenciadorlehsa.service.interfaces;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface BaseCRUDService<I> {

    I encontrarPorId(@NotNull UUID id);

    void deletar(@NotNull UUID id);

    List<I> listarTodos();
}
