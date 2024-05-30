package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.Item;
import org.springframework.context.ApplicationEvent;

public class DeletarItemEmTransacoesEvent extends ApplicationEvent {

    private Item item;


    public DeletarItemEmTransacoesEvent (Object source, Item item) {
        super (source);
        this.item = item;
    }
}
