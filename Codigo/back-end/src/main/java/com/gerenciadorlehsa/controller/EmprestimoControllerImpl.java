package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.controller.interfaces.OperacoesCRUDController;
import com.gerenciadorlehsa.dto.EmprestimoDTO;
import com.gerenciadorlehsa.dto.EmprestimoDTORes;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.ENDPOINT_EMPRESTIMO;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.EMPRESTIMO_CONTROLLER;

@Slf4j(topic = EMPRESTIMO_CONTROLLER)
@RestController
@Validated
@RequestMapping(ENDPOINT_EMPRESTIMO)
@AllArgsConstructor
public class EmprestimoControllerImpl implements OperacoesCRUDController<EmprestimoDTO, EmprestimoDTORes> {


    @Override
    public ResponseEntity<EmprestimoDTORes> encontrarPorId (UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> criar (EmprestimoDTO obj) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> atualizar (UUID id, EmprestimoDTO obj) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> deletar (UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<List<EmprestimoDTORes>> listarTodos () {
        return null;
    }
}
