package com.gerenciadorlehsa.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciadorlehsa.entity.enums.TipoItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;


@Entity
@Table(name = Item.NOME_TABELA)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Item {

    public static final String NOME_TABELA = "item";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private UUID id;

    @Column(name = "tipo_de_item", nullable = false)
    @JsonProperty("tipo_item")
    @Enumerated(EnumType.STRING)
    private TipoItem tipoItem;

    @Column(name = "quantidade", nullable = false)
    @Min(0)
    private Integer quantidade;

    @Column(name = "valor_unitario", nullable = false)
    @JsonProperty("valor_unitario")
    @DecimalMin(value = "0.0", inclusive = false)
    private Float valorUnitario;

    @Column(name = "nome", length = 64, nullable = false)
    @Size(min = 3, max = 64, message = "Nome deve ter entre 3 a 64 caracteres")
    private String nome;

    @Column(name = "emprestavel", nullable = false)
    private Boolean emprestavel;
}
