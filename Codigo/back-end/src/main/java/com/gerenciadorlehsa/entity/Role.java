package com.gerenciadorlehsa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gerenciadorlehsa.permissions.PermissionEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING) //Mapeia o texto e não 0 ou 1 para o BD
    private PermissionEnum nome;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private List<User> users;
}
