package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.service.ItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.ENDPOINT_ITEM;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.ITEM_CONTROLLER;


@Slf4j(topic = ITEM_CONTROLLER)
@RestController
@Validated
@RequestMapping(ENDPOINT_ITEM)
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<Item> encontrarPorId (@PathVariable Long id) {
        log.info(">>> encontrarPorId: recebendo requisição para encontrar item por id");
        Item item = this.itemService.encontrarPorId(id);
        return ResponseEntity.ok().body(item);
    }

    @GetMapping
    public ResponseEntity<List<Item>> listarTodos () {
        log.info(">>> listarTodos: recebendo requisição para listar todos itens");
        List<Item> itens = this.itemService.listarTodos();
        return ResponseEntity.ok().body(itens.stream().toList());
    }

    @PostMapping
    public ResponseEntity<Void> criar (@Valid @RequestBody Item item) {
        log.info(">>> criar: recebendo requisição para criar item");
        Item novoItem = this.itemService.criar(item);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(novoItem.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar (@PathVariable Long id,
                                           @Valid @RequestBody @NotNull Item item) {
        log.info(">>> atualizar: recebendo requisição para atualizar item");
        item.setId(id);
        this.itemService.atualizar(item);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar (@PathVariable Long id) {
        log.info(">>> deletar: recebendo requisição para deletar item");
        this.itemService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
