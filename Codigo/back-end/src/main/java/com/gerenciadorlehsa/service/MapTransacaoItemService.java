package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.service.interfaces.OperationsMapTransacaoItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MapTransacaoItemService {

    private final OperationsMapTransacaoItemService<Agendamento> operationsMapTransacaoItemService;

    // Injetar o de empréstimo

    public void deletarItensAssociados(Item item) {
        operationsMapTransacaoItemService.deletarItensAssociados (item);
        // Chamar o de empréstimo
    }


}
