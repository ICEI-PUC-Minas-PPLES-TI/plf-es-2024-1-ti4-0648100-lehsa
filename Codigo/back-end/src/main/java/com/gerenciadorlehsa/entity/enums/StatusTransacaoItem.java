package com.gerenciadorlehsa.entity.enums;

public enum StatusTransacaoItem {
    EM_ANALISE,
    APROVADO, // Admin
    RECUSADO, // Admin
    CANCELADO, // Usuário pode cancelar
    CONFIRMADO, //unico valor utilizado por usuário comum
    NAO_COMPARECEU, //Admin
    CONCLUIDO // Admin
}
