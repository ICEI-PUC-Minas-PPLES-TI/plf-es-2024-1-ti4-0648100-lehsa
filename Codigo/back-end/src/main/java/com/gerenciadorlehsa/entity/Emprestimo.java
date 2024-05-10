package com.gerenciadorlehsa.entity;


import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "endereco_id", referencedColumnName = "ID", nullable = false)
    private Endereco localUso;

    @Transient
    private Integer diasAtrasado;

    @Transient
    private Integer diasRestantes;

}
