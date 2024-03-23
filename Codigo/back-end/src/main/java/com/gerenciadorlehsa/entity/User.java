package com.gerenciadorlehsa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.gerenciadorlehsa.util.ConstantesErroValidadorUtil.*;

@Entity
@Table(name = "TB_USUARIO")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends RepresentationModel<User> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private UUID id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "EMAIL", unique = true, nullable = false)
    @Pattern(regexp = "^[a-z0-9.]+@[a-z0-9]+\\.[a-z]+\\.?([a-z]+)?$", message = MSG_ERRO_EMAIL)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 200, message = MSG_ERRO_SENHA)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "TELEFONE", nullable = false)
    @Pattern(regexp = "(^[0-9]{2})?(\\s|-)?(9?[0-9]{4})-?([0-9]{4}$)", message = MSG_ERRO_TELEFONE)
    private String telefone;

    @CPF
    @Column(name = "CPF", unique = true, nullable = false)
    @Pattern(regexp = "(^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$)", message = MSG_ERRO_CPF)
    private String cpf;


    @Column(name = "PERFIL_USUARIO", nullable = false)
    @JsonProperty("perfil_usuario")
    private Integer perfilUsuario;

}
