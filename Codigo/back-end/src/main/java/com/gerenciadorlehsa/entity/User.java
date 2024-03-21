package com.gerenciadorlehsa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciadorlehsa.entity.enums.ProfileEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuarios")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "full_name", length = 45)
    @NotBlank
    @Size(min = 5, max = 45)
    private String fullName;

    @Column(unique = true, length = 50)
    private String username;

    @Column(name = "password", length = 100)
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(length = 45, unique = true)
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "email deve ser um endereço de email válido")
    private String email;


    @NotBlank(message = "CPF é obrigatório")
    @CPF
    private String cpf;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "usuario_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"),
    uniqueConstraints = @UniqueConstraint (columnNames = {"usuario_id", "role_id"})) //um usuário não terá a mesma
    // role duas vezes
    private List<Role> roles;



/*    @ElementCollection(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @CollectionTable(name = "user_profile")
    @Column(name = "profile", nullable = false)
    private Set<Integer> profiles = new HashSet<> ();


    public Set<ProfileEnum> getProfile() {
        return this.profiles.stream().map(ProfileEnum :: toEnum).collect(Collectors.toSet());
    }

    public void addProfile(ProfileEnum usuarioEnum) {

        this.profiles.add(usuarioEnum.getCode());
    }*/



}
