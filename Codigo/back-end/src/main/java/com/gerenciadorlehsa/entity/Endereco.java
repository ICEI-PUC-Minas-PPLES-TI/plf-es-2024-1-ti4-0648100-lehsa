package com.gerenciadorlehsa.entity;


import com.gerenciadorlehsa.entity.enums.UF;
import com.gerenciadorlehsa.util.ConstantesErroValidadorUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.gerenciadorlehsa.util.ConstantesErroValidadorUtil.MSG_ERRO_CEP;

@Entity
@Table(name = "TB_ENDERECO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Endereco extends BaseEntity {

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
    @NotBlank(message = "CEP não pode estar em branco")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = MSG_ERRO_CEP)
    private String cep;

}
