package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.Item;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class ItemEvents {

    @Getter
    public static class DeletarItemEmTransacoesEvent extends ApplicationEvent {

        private final Item item;
        public DeletarItemEmTransacoesEvent (Object source, Item item) {
            super (source);
            this.item = item;
        }
    }
}
