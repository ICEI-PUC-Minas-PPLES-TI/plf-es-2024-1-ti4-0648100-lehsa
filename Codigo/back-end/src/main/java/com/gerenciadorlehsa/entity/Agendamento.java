package com.gerenciadorlehsa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
//@EntityListeners(AgendamentoListener.class)
@Table(name = "TB_AGENDAMENTO")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Agendamento extends TransacaoItem {

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tecnico_id")
    private User tecnico;

    // CascadeType.PERSIST = operações de persistência (criação)
    // CascadeType.MERGE = operações de mesclagem (atualização)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "agendamento_usuario",
            joinColumns = @JoinColumn(name = "agendamento_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<User> solicitantes;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "agendamento_item",
            joinColumns = @JoinColumn(name = "agendamento_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> itens;


}
