package com.gerenciadorlehsa.entity;


import com.gerenciadorlehsa.entity.enums.TipoItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.gerenciadorlehsa.util.ConstantesErroValidadorUtil.MSG_ERRO_SENHA;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_de_item", nullable = false)
    private TipoItem tipoItem;

    @Column(name = "quantidade", nullable = false)
    @Min(0)
    private int quantidade;

    @Column(name = "valor_unitario", nullable = false)
    @DecimalMin(value = "0.0", inclusive = false)
    private float valorUnitario;

    @Column(name = "nome", length = 64, nullable = false)
    @Size(min = 3, max = 64, message = "Mensagem ainda a definir")
    private String nome;

    @Column(name = "emprestavel", nullable = false)
    private boolean emprestavel;
}
