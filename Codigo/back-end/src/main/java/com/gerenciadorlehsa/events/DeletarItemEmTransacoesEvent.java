package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.Item;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeletarItemEmTransacoesEvent extends ApplicationEvent {

    private Item item;

    public DeletarItemEmTransacoesEvent (Object source, Item item) {
        super (source);
        this.item = item;
    }
}
