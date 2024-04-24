package com.gerenciadorlehsa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciadorlehsa.entity.enums.StatusCurso;
import com.gerenciadorlehsa.entity.enums.TipoCurso;
import com.gerenciadorlehsa.entity.enums.TipoItem;
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
public class User extends Pessoa {

    @Column(name = "PASSWORD", nullable = false)
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 200, message = MSG_ERRO_SENHA)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    @Column(name = "PERFIL_USUARIO", nullable = false)
    @JsonProperty("perfil_usuario")
    private Integer perfilUsuario;

    @Column(name = "CURSO")
    private String curso;

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
