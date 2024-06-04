package com.gerenciadorlehsa.repository;

import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Endereco;
import com.gerenciadorlehsa.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface EmprestimoRepository extends TransacaoRepository<Emprestimo>  {

    int countEmprestimoByLocalUso(Endereco localUso);

}
