package com.gerenciadorlehsa.service.interfaces;


import com.gerenciadorlehsa.security.UsuarioDetails;
import java.util.UUID;

/**
 * Valida a autorização do usuário logado para realizar uma determinada requisição
 */
public interface ValidadorAutorizacaoRequisicaoService {

    UsuarioDetails validarAutorizacaoRequisicao(UUID id, String topico);

    UsuarioDetails getUsuarioLogado();

    UsuarioDetails validarAutorizacaoRequisicao();
}
