package com.gerenciadorlehsa.service.interfaces;

import com.gerenciadorlehsa.entity.User;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Entidade com os métodos padrões do service
 *
 * @param <I> Classe do service
 */
public interface OperacoesCRUDService<I> {

    I encontrarPorId(@NotNull UUID id);

    I criar(@NotNull I obj);

    I atualizar(@NotNull I obj);

    void deletar(@NotNull UUID id);

    List<I> listarTodos();
}
