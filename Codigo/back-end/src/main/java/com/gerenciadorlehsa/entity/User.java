package com.gerenciadorlehsa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciadorlehsa.entity.enums.ProfileEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.util.HashSet;
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

    @Column(name = "full_name", length = 45, nullable = false)
    @NotBlank
    @Size(min = 5, max = 45)
    private String fullName;


    @Column(name = "email", length = 45, nullable = false, unique = true)
    @NotBlank
    @Email(message = "E-mail inválido")
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    @NotBlank
    @Size(min = 6)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "CPF é obrigatório")
    @CPF
    @Column(name = "cpf")
    private String cpf;

    @ElementCollection(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @CollectionTable(name = "user_profile")
    @Column(name = "profile", nullable = false)
    private Set<Integer> profiles = new HashSet<> ();


    public Set<ProfileEnum> getProfile() {
        return this.profiles.stream().map(ProfileEnum :: toEnum).collect(Collectors.toSet());
    }

    public void addProfile(ProfileEnum usuarioEnum) {

        this.profiles.add(usuarioEnum.getCode());
    }


}
