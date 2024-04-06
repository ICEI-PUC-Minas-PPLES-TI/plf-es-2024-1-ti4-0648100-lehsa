package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.enums.TipoItem;
import com.gerenciadorlehsa.exceptions.lancaveis.DeletarEntidadeException;
import com.gerenciadorlehsa.exceptions.lancaveis.EntidadeNaoEncontradaException;
import com.gerenciadorlehsa.exceptions.lancaveis.TipoItemNaoEncontradoException;
import com.gerenciadorlehsa.repository.ItemRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.ITEM_SERVICE;
import static java.lang.String.format;

@Service
@Slf4j(topic = ITEM_SERVICE)
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Item encontrarPorId(@NotNull UUID id) {
        log.info(">>> encontrarPorId: encontrando item por id");
        return this.itemRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(format("item não encontrado, id: %s", id)));
    }

    public List<Item> listarTodos() {
        log.info(">>> listarTodos: listando todos itens");
        return this.itemRepository.findAll();
    }

    @Transactional
    public Item criar (@NotNull Item item) {
        log.info(">>> criar: criando item");
        item.setId(null);
        item = this.itemRepository.save(item);
        log.info(format(">>> criar: item criado, id: %s", item.getId()));
        return item;
    }

    @Transactional
    public Item atualizar (Item item) {
        log.info(">>> atualizar: atualizando item");
        Item itemExistente = encontrarPorId(item.getId());

        BeanUtils.copyProperties(item, itemExistente, getNullPropertyNames(item));
        itemExistente = this.itemRepository.save(itemExistente);
        log.info(format(">>> atualizar: item atualizado, id: %s", item.getId()));
        return itemExistente;
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());

        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public void deletar (@NotNull UUID id) {
        log.info(">>> deletar: deletando item");
        encontrarPorId(id);
        try {
            this.itemRepository.deleteById(id);
            log.info(format(">>> deletar: item deletado, id: %s", id));
        } catch (Exception e) {
            throw new DeletarEntidadeException(format("existem entidades relacionadas: %s", e));
        }
    }

    public List<Item> encontrarPorTipo (@NotNull String tipo) {
        log.info(">>> encontrarPorTipo: encontrando itens com o tipo especificado");
        tipo = tipo.toUpperCase();
        try {
            TipoItem enumTipo = Enum.valueOf(TipoItem.class, tipo);
            return this.itemRepository.findByTipoItem(enumTipo);
        } catch (IllegalArgumentException e) {
            throw  new TipoItemNaoEncontradoException(format("não existe o tipo passado: " + tipo));
        }
    }

    public List<Item> encontrarPorNome (@NotNull String nome) {
        log.info(">>> encontrarPorNome: encontrando itens com o nome especificado");
        return this.itemRepository.findByNome(nome);
    }
}
