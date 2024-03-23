package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Role;
import com.gerenciadorlehsa.permissions.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    Role findByNome(PermissionEnum nome);
}
