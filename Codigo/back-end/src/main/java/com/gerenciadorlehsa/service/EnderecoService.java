package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Endereco;
import com.gerenciadorlehsa.exceptions.lancaveis.DeletarEntidadeException;
import com.gerenciadorlehsa.exceptions.lancaveis.EntidadeNaoEncontradaException;
import com.gerenciadorlehsa.repository.EnderecoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public Endereco encontrarPorId (UUID id) {
        return enderecoRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException(
                String.format("Endereco não encontrado, id: %s", id)));
    }

    public void deletar (UUID id) {
        encontrarPorId(id);
        try {
            enderecoRepository.deleteById(id);
        } catch (Exception e) {
            throw new DeletarEntidadeException(format("existem entidades relacionadas: %s", e));
        }
    }

    public List<Endereco> encontrarPorCep (String cep) {
        return enderecoRepository.findByCep(cep);
    }

}
