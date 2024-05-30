package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Transacao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
@Setter
public class ValidarMapaTransacaoItemEvent<T extends Transacao> extends ApplicationEvent {

    private UUID transacaoId;

    private T transacao;


    public ValidarMapaTransacaoItemEvent (Object source, T transacao, UUID transacaoId) {
        super (source);
        this.transacao = transacao;
        this.transacaoId = transacaoId;
    }
}
