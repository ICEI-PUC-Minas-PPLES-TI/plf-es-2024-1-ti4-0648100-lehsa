package com.gerenciadorlehsa.service.interfaces;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public interface PessoaService<I> {
    I encontrarPorEmail(@NotNull String email);

    I confirmaEmail(UUID id, Boolean value);

    void enviarEmail(I professor, String linkConfirmacao);
}
