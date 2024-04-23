package com.gerenciadorlehsa.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "TB_AGENDAMENTO")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Agendamento extends TransacaoItem {

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private User tecnico;

    @ManyToMany
    @JoinTable(
            name = "agendamento_usuario",
            joinColumns = @JoinColumn(name = "agendamento_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<User> solicitantes;


    @ManyToMany
    @JoinTable(
            name = "agendamento_item",
            joinColumns = @JoinColumn(name = "agendamento_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> itens;


}
