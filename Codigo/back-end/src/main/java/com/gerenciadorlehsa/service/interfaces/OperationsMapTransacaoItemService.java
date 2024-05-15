package com.gerenciadorlehsa.service.interfaces;

import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Transacao;

public interface OperationsMapTransacaoItemService<T extends Transacao> {
    void deletarItensAssociados(Item item);
}
