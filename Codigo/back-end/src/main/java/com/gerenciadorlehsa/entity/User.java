package com.gerenciadorlehsa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciadorlehsa.entity.enums.StatusCurso;
import com.gerenciadorlehsa.entity.enums.TipoCurso;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;
import static com.gerenciadorlehsa.util.ConstantesErroValidadorUtil.*;

@Entity
@Table(name = "TB_USUARIO")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends Pessoa {

    @Column(name = "PASSWORD", nullable = false)
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 200, message = MSG_ERRO_SENHA)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


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


    @OneToMany(mappedBy = "tecnico")
    private List<Agendamento> agendamentosComoTecnico;


    @ManyToMany(mappedBy = "solicitantes")
    private List<Agendamento> agendamentosRealizados;

}
