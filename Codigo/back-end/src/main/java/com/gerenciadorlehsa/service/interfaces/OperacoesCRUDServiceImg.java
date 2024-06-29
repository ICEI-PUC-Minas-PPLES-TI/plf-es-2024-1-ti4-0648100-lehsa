package com.gerenciadorlehsa.service.interfaces;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

public interface OperacoesCRUDServiceImg <I> extends BaseCRUDService<I>{

    I criar(@NotNull I obj, @NotNull MultipartFile img);

    I atualizar(@NotNull I obj, MultipartFile img);

    byte[] encontrarImagemPorId (@NotNull UUID id);
}
