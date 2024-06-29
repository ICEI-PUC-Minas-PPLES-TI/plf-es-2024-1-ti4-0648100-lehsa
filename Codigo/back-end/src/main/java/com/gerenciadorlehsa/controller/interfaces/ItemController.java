package com.gerenciadorlehsa.controller.interfaces;

import com.gerenciadorlehsa.dto.ItemDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ItemController {

    @GetMapping("/tipo/{tipo}")
    ResponseEntity<List<ItemDTO>> encontrarPorTipo (@PathVariable String tipo);

    @GetMapping("/nome/{nome}")
    ResponseEntity<List<ItemDTO>> encontrarPorNome (@PathVariable String nome);

    @GetMapping("/emprestaveis")
    ResponseEntity<List<ItemDTO>> listarEmprestaveis ();
}
