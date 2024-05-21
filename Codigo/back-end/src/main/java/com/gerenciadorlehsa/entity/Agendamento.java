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
    @ToString.Exclude
    private User tecnico;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "agendamento_usuario",
            joinColumns = @JoinColumn(name = "agendamento_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @ToString.Exclude
    private List<User> solicitantes;

    @ElementCollection
    @JsonIgnore
    @CollectionTable(name = "AGENDAMENTO_ITEM_QUANTIDADE", joinColumns = @JoinColumn(name = "AGENDAMENTO_ID"))
    @MapKeyJoinColumn(name = "ITEM_ID")
    @Column(name = "QUANTIDADE")
    private Map<Item, Integer> itensQuantidade = new HashMap<> ();


    @ManyToOne
    @JoinColumn(name = "professor_id"/*, nullable = false*/)
    @ToString.Exclude
    private Professor professor;


    @PreRemove
    @Override
    public void preRemove() {
        itensQuantidade.clear();
    }


    @Override
    public Map<Item, Integer> getItensQuantidade() {return itensQuantidade;}

    @Override
    public void setItensQuantidade (Map<Item, Integer> itensQuantidade) {
        this.itensQuantidade = itensQuantidade;
    }


}

