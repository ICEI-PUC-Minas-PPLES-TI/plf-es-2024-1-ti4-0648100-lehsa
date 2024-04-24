package com.gerenciadorlehsa.service.interfaces;


import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface OperacoesAdminService<I> {


    List<I> listarTodos();


    void atualizarPerfil(@NotNull UUID id, Integer code);

}
