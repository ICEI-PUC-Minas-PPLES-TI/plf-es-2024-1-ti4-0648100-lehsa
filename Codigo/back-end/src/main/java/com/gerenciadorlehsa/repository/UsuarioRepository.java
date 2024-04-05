package com.gerenciadorlehsa.repository;


import com.gerenciadorlehsa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<User, UUID> {

    /**
     * Busca um usuário a partir do seu email
     *
     * @param email email do usuário
     * @return usuário buscado
     */
    @Transactional(readOnly = true)
    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);


    /**
     * Busca a senha de um usuário a partir do seu id
     * @param id id do usuário
     * @return senha do usuário
     */
    @Transactional(readOnly = true)
    @Query("SELECT u.password FROM User u WHERE u.id = :id")
    String buscarSenhaUsuarioPorId(UUID id);

    /**
     * Atualiza a senha de um usuário
     *
     * @param senha senha atualizada
     * @param id    id do usuário
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :senha WHERE u.id = :id")
    void atualizarSenhaUsuario(String senha, UUID id);
}
