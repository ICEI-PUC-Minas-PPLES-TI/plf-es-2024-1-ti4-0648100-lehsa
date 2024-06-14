package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Endereco;
import com.gerenciadorlehsa.exceptions.lancaveis.DeletarEntidadeException;
import com.gerenciadorlehsa.exceptions.lancaveis.EntidadeNaoEncontradaException;
import com.gerenciadorlehsa.repository.EnderecoRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

    public Endereco criar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }


    public void deletar (UUID id) {
        encontrarPorId(id);
        try {
            enderecoRepository.deleteById(id);
        } catch (Exception e) {
            throw new DeletarEntidadeException(format("existem entidades relacionadas: %s", e));
        }
    }

    public Endereco enderecoExiste(Endereco endereco) {
        Optional<Endereco> existingEndereco = enderecoRepository.findByEndereco(endereco);
        return existingEndereco.orElse(null);
    }


}
