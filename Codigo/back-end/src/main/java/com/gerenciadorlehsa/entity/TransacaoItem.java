package com.gerenciadorlehsa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciadorlehsa.entity.enums.StatusTransacaoItem;
import com.gerenciadorlehsa.entity.enums.TipoItem;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public abstract class TransacaoItem implements Serializable {

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
}
