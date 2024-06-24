package com.gerenciadorlehsa.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "TB_EMPRESTIMO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Emprestimo extends Transacao {

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User solicitante;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id", referencedColumnName = "ID", nullable = false)
    private Endereco localUso;

    @ElementCollection
    @JsonIgnore
    @CollectionTable(name = "EMPRESTIMO_ITEM_QUANTIDADE", joinColumns = @JoinColumn(name = "EMPRESTIMO_ID"))
    @MapKeyJoinColumn(name = "ITEM_ID")
    @Column(name = "QUANTIDADE")
    private Map<Item, Integer> itensQuantidade = new HashMap<> ();

    @Transient
    private Integer diasAtrasado;

    @Transient
    private Integer diasRestantes;

    @PreRemove
    @Override
    public void preRemove() {
        itensQuantidade.clear();
    }

    @Override
    public Map<Item, Integer> getItensQuantidade() {
        return itensQuantidade;
    }

    @Override
    public void setItensQuantidade (Map<Item, Integer> itensQuantidade) {
        this.itensQuantidade = itensQuantidade;
    }

}
