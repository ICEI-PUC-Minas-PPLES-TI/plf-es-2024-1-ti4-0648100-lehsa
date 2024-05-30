package com.gerenciadorlehsa.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciadorlehsa.entity.enums.StatusTransacaoItem;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
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


    public abstract void preRemove();


    public abstract Map<Item, Integer> getItensQuantidade();

    public abstract void setItensQuantidade(Map<Item, Integer> itensQuantidade);

}
