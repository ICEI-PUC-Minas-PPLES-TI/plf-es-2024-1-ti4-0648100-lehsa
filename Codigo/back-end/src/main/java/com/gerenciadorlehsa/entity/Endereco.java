package com.gerenciadorlehsa.entity;


import com.gerenciadorlehsa.entity.enums.UF;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "TB_ENDERECO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endereco implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private UUID id;

    @Column(name = "rua", length = 45, nullable = false)
    @NotBlank
    private String rua;

    @Column(name = "bairro", length = 45, nullable = false)
    @NotBlank
    private String bairro;

    @Column(name = "home_state", length = 45, nullable = false)
    @Enumerated(EnumType.STRING)
    private UF uf;

    @Column(name = "cidade", length = 45, nullable = false)
    @NotBlank
    private String cidade;

    @Column(name = "numero", length = 45, nullable = false)
    @NotNull
    private Integer numero;

    @Column(name = "complemento", length = 45)
    private String complemento;

    @Column(name = "cep", length = 45, nullable = false)
    @NotBlank
    private String cep;


}
