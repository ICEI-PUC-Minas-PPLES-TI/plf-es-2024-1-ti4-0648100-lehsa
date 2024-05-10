package com.gerenciadorlehsa.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.ENDPOINT_EMPRESTIMO;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.EMPRESTIMO_CONTROLLER;

@Slf4j(topic = EMPRESTIMO_CONTROLLER)
@RestController
@Validated
@RequestMapping(ENDPOINT_EMPRESTIMO)
@AllArgsConstructor
public class EmprestimoControllerImpl {
}
