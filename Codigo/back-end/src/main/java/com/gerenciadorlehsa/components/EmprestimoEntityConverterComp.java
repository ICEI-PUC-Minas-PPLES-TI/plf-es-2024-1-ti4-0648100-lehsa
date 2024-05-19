package com.gerenciadorlehsa.components;

import com.gerenciadorlehsa.components.abstracts.TransacaoDTOValidadadorComp;
import com.gerenciadorlehsa.components.abstracts.TransacaoEntityConverterComp;
import com.gerenciadorlehsa.dto.EmprestimoDTO;
import com.gerenciadorlehsa.dto.EnderecoDTO;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Endereco;
import com.gerenciadorlehsa.service.ItemService;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.gerenciadorlehsa.entity.enums.StatusTransacaoItem.EM_ANALISE;
import static com.gerenciadorlehsa.util.DataHoraUtil.converterDataHora;

@Component
public class EmprestimoEntityConverterComp extends TransacaoEntityConverterComp<Emprestimo, EmprestimoDTO> {

    TransacaoDTOValidadadorComp<EmprestimoDTO> emprestimoDTOValidadadorComp;

    @Autowired
    public EmprestimoEntityConverterComp (UsuarioService usuarioService, ItemService itemService, TransacaoDTOValidadadorComp<EmprestimoDTO> emprestimoDTOValidadadorComp) {
        super (usuarioService, itemService);
        this.emprestimoDTOValidadadorComp = emprestimoDTOValidadadorComp;
    }

    @Override
    public Emprestimo convertToEntity(EmprestimoDTO emprestimoDTO) {
        emprestimoDTOValidadadorComp.validate (emprestimoDTO);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(emprestimoDTO.id());
        emprestimo.setDataHoraInicio(converterDataHora (emprestimoDTO.dataHoraInicio ()));
        emprestimo.setDataHoraFim(converterDataHora (emprestimoDTO.dataHoraFim ()));
        emprestimo.setObservacaoSolicitacao(emprestimoDTO.observacaoSolicitacao());
        emprestimo.setStatusTransacaoItem(EM_ANALISE);
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
