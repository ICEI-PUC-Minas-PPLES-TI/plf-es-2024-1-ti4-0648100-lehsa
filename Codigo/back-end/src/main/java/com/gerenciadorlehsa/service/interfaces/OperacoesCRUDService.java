package com.gerenciadorlehsa.service.interfaces;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * Entidade com os métodos padrões do service
 *
 * @param <I> Classe do service
 */
public interface OperacoesCRUDService<I> extends BaseCRUDService<I> {
    I criar(@NotNull I obj);

    I atualizar(@NotNull I obj);
}
