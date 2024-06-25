package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.dto.EmprestimoDTO;
import com.gerenciadorlehsa.dto.EnderecoDTO;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.gerenciadorlehsa.entity.enums.StatusTransacao.EM_ANALISE;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.EMPRESTIMO_ENTITY_CONVERTER_COMP;
import static com.gerenciadorlehsa.util.DataHoraUtil.converterDataHora;

@Slf4j(topic = EMPRESTIMO_ENTITY_CONVERTER_COMP)
@Component
@AllArgsConstructor
@Schema(description = "Responsável por converter DTO de empréstimo em entidade empréstimo")
public class EmprestimoEntityConverterService extends TransacaoEntityConverterService<Emprestimo, EmprestimoDTO> {

    private final TransacaoDTOValidadadorService<EmprestimoDTO> emprestimoDTOValidadadorComp;

    @Override
    public Emprestimo convertToEntity(EmprestimoDTO emprestimoDTO) {
        emprestimoDTOValidadadorComp.validate (emprestimoDTO);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(emprestimoDTO.id());
        emprestimo.setDataHoraInicio(converterDataHora (emprestimoDTO.dataHoraInicio ()));
        emprestimo.setDataHoraFim(converterDataHora (emprestimoDTO.dataHoraFim ()));
        emprestimo.setObservacaoSolicitacao(emprestimoDTO.observacaoSolicitacao());
        emprestimo.setStatusTransacao (EM_ANALISE);
        emprestimo.setSolicitante(acharSolicitante(emprestimoDTO.solicitante()));
        emprestimo.setItensQuantidade(convertMapa(emprestimoDTO.itens ()));
        emprestimo.setLocalUso(convertToEntity(emprestimoDTO.endereco()));
        return emprestimo;
    }

    //a mudar
    private Endereco convertToEntity(EnderecoDTO enderecoDTO) {
        Endereco endereco = new Endereco();
        endereco.setId(enderecoDTO.id());
        endereco.setUf(enderecoDTO.uf());
        endereco.setRua(enderecoDTO.rua());
        endereco.setBairro(enderecoDTO.bairro());
        endereco.setCidade(enderecoDTO.cidade());
        endereco.setNumero(enderecoDTO.numero());
        endereco.setComplemento(enderecoDTO.complemento());
        endereco.setCep(enderecoDTO.cep());

        return endereco;
    }
}
