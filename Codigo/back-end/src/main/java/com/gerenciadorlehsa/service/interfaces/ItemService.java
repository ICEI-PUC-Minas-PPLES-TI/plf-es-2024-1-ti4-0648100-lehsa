package com.gerenciadorlehsa.service.interfaces;

import com.gerenciadorlehsa.entity.Item;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface ItemService {

    List<Item> encontrarPorTipo (@NotNull String tipo);

    List<Item> listarEmprestaveis ();

    List<Item> encontrarPorNome (@NotNull String nome);
}
