package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Endereco;
import com.gerenciadorlehsa.exceptions.lancaveis.DeletarEntidadeException;
import com.gerenciadorlehsa.exceptions.lancaveis.EntidadeNaoEncontradaException;
import com.gerenciadorlehsa.repository.EnderecoRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.lang.String.format;

@Service
@AllArgsConstructor
@Schema(description = "Contém as regras de negócio para endereço")
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

    public boolean saoIguais (Endereco e1, Endereco e2) {
        if (e1 == e2) return true;
        if (e1 == null || e2 == null) return false;

        return Objects.equals(e1.getRua(), e2.getRua()) &&
                Objects.equals(e1.getBairro(), e2.getBairro()) &&
                e1.getUf() == e2.getUf() &&
                Objects.equals(e1.getCidade(), e2.getCidade()) &&
                Objects.equals(e1.getNumero(), e2.getNumero()) &&
                Objects.equals(e1.getComplemento(), e2.getComplemento()) &&
                Objects.equals(e1.getCep(), e2.getCep());
    }

}
