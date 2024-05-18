package com.gerenciadorlehsa.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_PROFESSOR")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Professor extends Pessoa{

    @Column(name = "MATRICULA", unique = true, nullable = false)
    @NotBlank(message = "A matricula SIAPE é obrigatória")
    private String matricula;

    @Column(name = "CAMPUS", nullable = false)
    @NotBlank(message = "O campus é obrigatório")
    private String campus;

    @Column(name = "LOTACAO", nullable = false)
    @NotBlank(message = "A lotação é obrigatória")
    private String lotacao;

    @Column(name = "AREA_ATUACAO",nullable = false)
    @NotBlank(message = "A área de atuação do professor é obrigatória")
    private String areaAtuacao;

    @Column(name = "LABORATORIO", nullable = false)
    @NotBlank(message = "O laboratório pelo qual o professor é vinculado é obrigatório")
    private String laboratorio;

    @Column(name = "CONFIRMA_CADASTRO", nullable = false)
    private Boolean confirmaCadastro;

    @Column(name = "DATA_CRIACAO", nullable = false)
    private LocalDateTime dataHoraCriacao;

    //private String foto;
}
