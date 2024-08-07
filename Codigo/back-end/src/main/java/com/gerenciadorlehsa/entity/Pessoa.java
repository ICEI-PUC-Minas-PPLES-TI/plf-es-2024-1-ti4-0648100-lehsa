package com.gerenciadorlehsa.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import static com.gerenciadorlehsa.util.ConstantesErroValidadorUtil.*;


@MappedSuperclass
@Getter
@Setter
@Schema(description = "Superclasse para entidades Pessoa")
public abstract class Pessoa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Pessoa extends BaseEntity {

    @Column(name = "NOME", length = 100,nullable = false)
    private String nome;

    @Column(name = "EMAIL", unique = true, nullable = false)
    @Pattern(regexp = "^[a-z0-9.]+@[a-z0-9]+\\.[a-z]+\\.?([a-z]+)?$", message = MSG_ERRO_EMAIL)
    private String email;

}
