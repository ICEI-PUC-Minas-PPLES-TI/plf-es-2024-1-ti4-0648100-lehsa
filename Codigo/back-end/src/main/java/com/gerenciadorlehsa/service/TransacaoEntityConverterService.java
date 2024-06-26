package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.dto.ItemDTO;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Transacao;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.exceptions.lancaveis.TransacaoException;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDServiceImg;
import com.gerenciadorlehsa.service.interfaces.UsuarioService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public abstract class TransacaoEntityConverterService<T extends Transacao,DTO> {

    @Autowired
    protected UsuarioService usuarioService;

    @Autowired
    private OperacoesCRUDServiceImg<Item> operacoesCRUDServiceImg;


    public abstract T convertToEntity(DTO dto);

    protected Map<Item, Integer> convertMapa(List<ItemDTO> itemDTOS) {
        List<Integer> quantidade = itemDTOS
                .stream ()
                .map (ItemDTO::quantidadeTransacao)
                .toList ();
        List<Item> chaves = acharItens (itemDTOS);

        if(chaves.size () != quantidade.size ())
            throw new TransacaoException ("Quantidade de itens e número de unidades de cada item difere");

        Map<Item, Integer> mapa = new HashMap<> ();
        for (int i = 0; i < chaves.size(); i++) {
            mapa.put(chaves.get(i), quantidade.get(i));
        }
        return mapa;
    }


    protected List<Item> acharItens(List<ItemDTO> itensDTO) {

        return itensDTO.stream()
                .map(itemDTO -> operacoesCRUDServiceImg.encontrarPorId(itemDTO.id()))
                .collect(Collectors.toList());
    }

    protected User acharSolicitante(UsuarioDTO usuarioDTO) {
        if(usuarioDTO == null)
            throw new TransacaoException ("A transação precisa ter solicitante");
        return usuarioService.encontrarPorEmail (usuarioDTO.email ());
    }

}
