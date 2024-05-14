package com.gerenciadorlehsa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.annotations.CascadeType.ALL;

@Entity
@Table(name = "TB_AGENDAMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Agendamento extends Transacao {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tecnico_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User tecnico;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "agendamento_usuario",
            joinColumns = @JoinColumn(name = "agendamento_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<User> solicitantes;

  /*  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "agendamento_item",
            joinColumns = @JoinColumn(name = "agendamento_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> itens;*/

    @ElementCollection
    @JsonIgnore
    @CollectionTable(name = "AGENDAMENTO_ITEM_QUANTIDADE", joinColumns = @JoinColumn(name = "AGENDAMENTO_ID"))
    @MapKeyJoinColumn(name = "ITEM_ID")
    @Column(name = "QUANTIDADE")
    private Map<Item, Integer> itensQuantidade = new HashMap<> ();


    @PreRemove
    private void preRemove() {
        itensQuantidade.clear();
    }

}

