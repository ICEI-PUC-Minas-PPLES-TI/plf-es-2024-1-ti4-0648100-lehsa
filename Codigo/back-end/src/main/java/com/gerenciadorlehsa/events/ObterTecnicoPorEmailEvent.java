package com.gerenciadorlehsa.events;

import com.gerenciadorlehsa.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ObterTecnicoPorEmailEvent extends ApplicationEvent {

    private String email;
    private User user;

    public ObterTecnicoPorEmailEvent (Object source, String email) {
        super(source);
        this.email = email;
    }



}
