package com.gerenciadorlehsa.service.interfaces;


import com.gerenciadorlehsa.security.UserDetailsImpl;
import java.util.UUID;

/**
 * Valida a autorização do usuário logado para realizar uma determinada requisição
 */
public interface ValidadorAutorizacaoRequisicaoService {

    UserDetailsImpl validarAutorizacaoRequisicao(UUID id, String topico);

    UserDetailsImpl getUsuarioLogado();

    UserDetailsImpl validarAutorizacaoRequisicao();
}
