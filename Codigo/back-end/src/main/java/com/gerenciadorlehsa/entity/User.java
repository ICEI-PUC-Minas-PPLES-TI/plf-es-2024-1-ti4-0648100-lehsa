package com.gerenciadorlehsa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciadorlehsa.entity.enums.StatusCurso;
import com.gerenciadorlehsa.entity.enums.TipoCurso;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;
import static com.gerenciadorlehsa.util.ConstantesErroValidadorUtil.*;

@Entity
@Table(name = "TB_USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"agendamentosComoTecnico", "agendamentosRealizados", "emprestimos"})
public class User extends Pessoa {

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

    @Column(name = "CURSO", length = 20)
    private String curso;

    @Column(name = "NOTA")
    @JsonIgnore
    private Double nota;

    @Column(name = "TIPO_CURSO")
    @JsonProperty("tipo_curso")
    @Enumerated(EnumType.STRING)
    private TipoCurso tipoCurso;

    @Column(name = "STATUS_CURSO")
    @JsonProperty("status_curso")
    @Enumerated(EnumType.STRING)
    private StatusCurso statusCurso;


    @OneToMany(mappedBy = "tecnico", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Agendamento> agendamentosComoTecnico;


    @ManyToMany(mappedBy = "solicitantes", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Agendamento> agendamentosRealizados;


    @OneToMany(mappedBy = "solicitante", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Emprestimo> emprestimos;


}


