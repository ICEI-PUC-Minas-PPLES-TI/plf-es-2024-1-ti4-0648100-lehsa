package com.gerenciadorlehsa.service.interfaces;

import com.gerenciadorlehsa.entity.Pessoa;
import org.jetbrains.annotations.NotNull;

public interface PessoaService<I extends Pessoa> {
    I encontrarPorEmail(@NotNull String email);
}
