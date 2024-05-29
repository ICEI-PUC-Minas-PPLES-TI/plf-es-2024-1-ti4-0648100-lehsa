package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.controller.interfaces.ItemController;
import com.gerenciadorlehsa.controller.interfaces.OperacoesCRUDControllerImg;
import com.gerenciadorlehsa.dto.ItemDTO;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.service.ItemServiceImpl;
import com.gerenciadorlehsa.service.interfaces.ItemService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDServiceImg;
import com.gerenciadorlehsa.util.ConversorEntidadeDTOUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.*;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.ITEM_CONTROLLER;
import static com.gerenciadorlehsa.util.ConstrutorRespostaJsonUtil.construirRespostaJSON;
import static com.gerenciadorlehsa.util.ConversorEntidadeDTOUtil.converterParaDTO;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@Slf4j(topic = ITEM_CONTROLLER)
@RestController
@Validated
@RequestMapping(ENDPOINT_ITEM)
@AllArgsConstructor
public class ItemControllerImpl implements ItemController, OperacoesCRUDControllerImg<Item, ItemDTO> {

    private final ItemService itemService;
    private final OperacoesCRUDServiceImg<Item> operacoesCRUDServiceImg;


    @Override
    @GetMapping("/img/{id}")
    public ResponseEntity<?> encontrarImagemPorId (@PathVariable UUID id) {
        log.info(">>> encontrarImagemPorId: recebendo requisição para encontrar imagem por id");
        byte [] img = operacoesCRUDServiceImg.encontrarImagemPorId(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/png"))
                .body(img);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> encontrarPorId (@PathVariable UUID id) {
        log.info(">>> encontrarPorId: recebendo requisição para encontrar item por id");
        Item item = operacoesCRUDServiceImg.encontrarPorId(id);
        return ResponseEntity.ok().body(converterParaDTO(item));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ItemDTO>> listarTodos () {
        log.info(">>> listarTodos: recebendo requisição para listar todos itens");
        List<Item> itens = operacoesCRUDServiceImg.listarTodos();
        return ResponseEntity.ok().body(itens.stream().map(ConversorEntidadeDTOUtil::converterParaDTO).toList());
    }

    @Override
    @GetMapping("/emprestaveis")
    public ResponseEntity<List<ItemDTO>> listarEmprestaveis () {
        log.info(">>> listarEmprestaveis: recebendo requisição para listar itens emprestaveis");
        List<Item> itens = itemService.listarEmprestaveis();
        return ResponseEntity.ok().body(itens.stream().map(ConversorEntidadeDTOUtil::converterParaDTO).toList());
    }

    @Override
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ItemDTO>> encontrarPorTipo (@PathVariable String tipo) {
        log.info(">>> encontrarPorTipo: recebendo requisição para encontrar itens por tipo");
        List<Item> itens = itemService.encontrarPorTipo(tipo);
        return ResponseEntity.ok().body(itens.stream().map(ConversorEntidadeDTOUtil::converterParaDTO).toList());
    }

    @Override
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<ItemDTO>> encontrarPorNome (@PathVariable String nome) {
        log.info(">>> encontrarPorNome: recebendo requisição para encontrar itens por nome");
        List<Item> itens = itemService.encontrarPorNome(nome);
        return ResponseEntity.ok().body(itens.stream().map(ConversorEntidadeDTOUtil::converterParaDTO).toList());
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> criar (@Valid @RequestPart("item") Item item,
                                                      @NotNull @RequestPart("imagem") MultipartFile img){
        log.info(">>> criar: recebendo requisição para criar item");    
        Item novoItem = operacoesCRUDServiceImg.criar(item, img);

        return ResponseEntity.created (URI.create("/item/" + novoItem.getId())).body (construirRespostaJSON(CHAVES_ITEM_CONTROLLER, asList(CREATED.value(), MSG_ITEM_CRIADO, novoItem.getId())));
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> atualizar (@PathVariable UUID id,
                                           @Valid @RequestPart("item") Item item,
                                            @RequestPart("imagem") MultipartFile img) {
        log.info(">>> atualizar: recebendo requisição para atualizar item");
        item.setId(id);
        Item itemAtt = operacoesCRUDServiceImg.atualizar(item, img);

        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_ITEM_CONTROLLER, asList(OK.value(),
                MSG_ITEM_ATUALIZADO, itemAtt.getId())));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar (@PathVariable UUID id) {
        log.info(">>> deletar: recebendo requisição para deletar item");
        operacoesCRUDServiceImg.deletar(id);

        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_ITEM_CONTROLLER, asList(OK.value(),
                MSG_ITEM_DELETADO, id)));
    }
}
