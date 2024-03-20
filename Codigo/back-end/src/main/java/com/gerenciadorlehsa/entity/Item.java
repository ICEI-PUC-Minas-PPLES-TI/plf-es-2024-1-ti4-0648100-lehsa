package com.gerenciadorlehsa.entity;


import com.gerenciadorlehsa.entity.enums.TipoItem;
import jakarta.persistence.*;
import lombok.*;

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
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_de_item", nullable = false)
    private TipoItem tipoItem;







}
