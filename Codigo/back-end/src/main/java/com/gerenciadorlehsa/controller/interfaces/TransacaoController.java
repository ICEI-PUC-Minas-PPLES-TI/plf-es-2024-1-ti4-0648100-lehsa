package com.gerenciadorlehsa.controller.interfaces;

import com.gerenciadorlehsa.entity.Transacao;
import com.gerenciadorlehsa.events.ValidarMapaTransacaoItemEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
import java.util.UUID;

public interface TransacaoController {
    @PatchMapping("/{id}/{status}")
    ResponseEntity<Map<String, Object>> atualizarStatus (@PathVariable UUID id,
                                                         @PathVariable String status);

    default <T extends Transacao> ValidarMapaTransacaoItemEvent<T> generateEvent(T transacao) {
        return new ValidarMapaTransacaoItemEvent<> (transacao,
                transacao,
                transacao.getId ());
    }

    default <T extends Transacao> ValidarMapaTransacaoItemEvent<T> generateEvent(T transacao, UUID id) {
        return new ValidarMapaTransacaoItemEvent<> (transacao,
                transacao,
                id);
    }

}
