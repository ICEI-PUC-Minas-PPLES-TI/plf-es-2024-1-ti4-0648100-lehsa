package com.gerenciadorlehsa.components;

import com.gerenciadorlehsa.components.abstracts.TransacaoDTOValidadadorComp;
import com.gerenciadorlehsa.dto.EmprestimoDTO;
import com.gerenciadorlehsa.dto.EnderecoDTO;
import com.gerenciadorlehsa.exceptions.lancaveis.EmprestimoException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.EMPRESTIMO_DTO_VALIDADOR_COMP;


@Slf4j(topic = EMPRESTIMO_DTO_VALIDADOR_COMP)
@Component
@AllArgsConstructor
@Schema(description = "validações relacionadas ao empréstimo")
public class EmprestimoDTOValidadorComp extends TransacaoDTOValidadadorComp<EmprestimoDTO> {
    @Override
    public void validate(EmprestimoDTO emprestimoDTO) {
        validarItens(emprestimoDTO.itens());
        validarSolicitante(emprestimoDTO.solicitante());
        validarEndereco(emprestimoDTO.endereco());
    }

    private void validarEndereco(EnderecoDTO enderecoDTO){
        if(enderecoDTO == null)
            throw new EmprestimoException("Emprestimo com endereco nulo");
        if(enderecoDTO.uf() == null)
            throw new EmprestimoException("Emprestimo com estado do endereco nulo");
        if(enderecoDTO.rua() == null)
            throw new EmprestimoException("Emprestimo com rua do endereco nulo");
        if(enderecoDTO.bairro() == null)
            throw new EmprestimoException("Emprestimo com bairro do endereco nulo");
        if(enderecoDTO.cidade() == null)
            throw new EmprestimoException("Emprestimo com cidade do endereco nulo");
        if(enderecoDTO.numero() == null)
            throw new EmprestimoException("Emprestimo com numero do endereco nulo");
        if(enderecoDTO.complemento() == null)
            throw new EmprestimoException("Emprestimo com complemento do endereco nulo");
        if(enderecoDTO.cep() == null)
            throw new EmprestimoException("Emprestimo com cep do endereco nulo");
    }
}
