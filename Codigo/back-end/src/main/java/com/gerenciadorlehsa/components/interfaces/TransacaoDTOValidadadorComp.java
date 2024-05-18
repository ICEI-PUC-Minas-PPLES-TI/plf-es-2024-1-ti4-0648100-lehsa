package com.gerenciadorlehsa.components.interfaces;


import com.gerenciadorlehsa.dto.ItemDTO;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.exceptions.lancaveis.TransacaoException;
import java.util.List;


public abstract class TransacaoDTOValidadadorComp<T> {


    public abstract void validate (T dto);


    protected void validarSolicitante(UsuarioDTO solicitante) {
        if(solicitante == null)
            throw new TransacaoException ("A transação não pode ter solicitante nulo");
        if (solicitante.email () == null)
            throw new TransacaoException ("A lista de solicitantes contém e-mails nulos");
    }


    protected void validarItens (List<ItemDTO> itensDTO) {

        if (itensDTO == null || itensDTO.isEmpty())
            throw new TransacaoException ("A transação tem que ter no mínimo 1 item");
        if (itensDTO.contains(null))
            throw new TransacaoException ("A lista de itens contém elementos nulos");
        if (itensDTO.stream().anyMatch(itemDTO -> itemDTO.id() == null))
            throw new TransacaoException ("A lista de itens contém IDs nulos");
        if(itensDTO.stream().anyMatch(itemDTO -> itemDTO.quantidade () == null))
            throw new TransacaoException ("A lista de itens contém quantidade nula");
        if (itensDTO.size() > 10)
            throw new TransacaoException ("O máximo de itens é 10");
    }


}
