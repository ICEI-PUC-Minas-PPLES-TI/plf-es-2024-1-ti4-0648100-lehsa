package com.gerenciadorlehsa.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciadorlehsa.entity.enums.StatusTransacao;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;



@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Transacao extends BaseEntity {

    @Column(name = "DATA_HORA_INICIO", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "DATA_HORA_FIM", nullable = false)
    private LocalDateTime dataHoraFim;

    @Column(name = "OBSERVACAO")
    private String observacaoSolicitacao;

    @Column(name = "STATUS", nullable = false)
    @JsonProperty("status_transacao")
    @Enumerated(EnumType.STRING)
    private StatusTransacao statusTransacao;

    public abstract void preRemove();

    public abstract Map<Item, Integer> getItensQuantidade();

    public abstract void setItensQuantidade(Map<Item, Integer> itensQuantidade);

}
