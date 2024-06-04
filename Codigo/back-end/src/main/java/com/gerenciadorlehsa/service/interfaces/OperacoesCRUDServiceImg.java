package com.gerenciadorlehsa.service.interfaces;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

public interface OperacoesCRUDServiceImg <I>{
    I encontrarPorId(@NotNull UUID id);

    I criar(@NotNull I obj, @NotNull MultipartFile img);

    I atualizar(@NotNull I obj, MultipartFile img);

    void deletar(@NotNull UUID id);

    List<I> listarTodos();

    byte[] encontrarImagemPorId (@NotNull UUID id);
}
