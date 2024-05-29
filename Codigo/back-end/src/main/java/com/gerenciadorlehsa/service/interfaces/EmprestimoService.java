package com.gerenciadorlehsa.service.interfaces;

import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Endereco;

import java.time.LocalDateTime;
import java.util.List;

public interface EmprestimoService {
    void deletarEmprestimoDaListaDoUsuario(Emprestimo emprestimo);

    void verificarConflitosDeEmprestimo(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim);

    void validarDataHoraAtt(List<String> atributosIguais, Emprestimo obj);

    void deletarEndereco (Endereco endereco);
}
