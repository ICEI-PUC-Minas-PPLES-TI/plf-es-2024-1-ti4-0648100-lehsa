package com.gerenciadorlehsa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciadorlehsa.entity.enums.StatusTransacaoItem;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@MappedSuperclass
@Getter
@Setter
public abstract class Transacao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private UUID id;


    @Column(name = "DATA_HORA_INICIO", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "DATA_HORA_FIM", nullable = false)
    private LocalDateTime dataHoraFim;

    @Column(name = "OBSERVACAO")
    private String observacaoSolicitacao;

    @Column(name = "STATUS", nullable = false)
    @JsonProperty("status_transacao")
    @Enumerated(EnumType.STRING)
    private StatusTransacaoItem statusTransacaoItem;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "transacao_item",
            joinColumns = @JoinColumn(name = "transacao_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> itens;


    @ElementCollection
    @JsonIgnore
    @CollectionTable(name = "TRANSACAO_ITEM_QUANTIDADE", joinColumns = @JoinColumn(name = "TRANSACAO_ID"))
    @MapKeyJoinColumn(name = "ITEM_ID")
    @Column(name = "QUANTIDADE")
    private Map<Item, Integer> itensQuantidade = new HashMap<> ();


}
