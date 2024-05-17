package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.repository.EmprestimoRepository;
import com.gerenciadorlehsa.security.UsuarioDetails;
import com.gerenciadorlehsa.service.interfaces.EmprestimoService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
public class EmprestimoServiceImpl extends TransacaoService<Emprestimo> implements OperacoesCRUDService<Emprestimo>, EmprestimoService{

    private final EmprestimoRepository emprestimoRepository;

    @Autowired
    public EmprestimoServiceImpl (ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService, EmprestimoRepository emprestimoRepository) {
        super (validadorAutorizacaoRequisicaoService);
        this.emprestimoRepository = emprestimoRepository;
    }

    @Override
    public Emprestimo encontrarPorId (UUID id) {
        return null;
    }

    @Override
    public Emprestimo criar (Emprestimo obj) {
        //validadorAutorizacaoRequisicaoService.getUsuarioLogado ();

        return null;
    }

    @Override
    public Emprestimo atualizar (Emprestimo obj) {
        return null;
    }

    @Override
    public void deletar (UUID id) {

    }

    @Override
    public List<Emprestimo> listarTodos () {
        return null;
    }

    @Override
    public void atualizarStatus (String status, UUID id) {

    }

    @Override
    public List<Emprestimo> transacoesAprovadasOuConfirmadasConflitantes (LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        return emprestimoRepository.findAprovadosOuConfirmadosConflitantes (dataHoraInicio, dataHoraFim);
    }

/*    public void verificarConflitoComTransacoesAprovadasOuConfirmadas (Emprestimo emprestimo){
        List<Item> itens = emprestimo.getItens ();
        List<Emprestimo> emprestimosAprovadosOuConfirmadosConflitantes =
                transacoesAprovadasOuConfirmadasConflitantes (emprestimo.getDataHoraInicio (), emprestimo.getDataHoraFim ());

        for (Item item : itens) {
            List<Emprestimo> emprestimosDoItem = item.getEmprestimos ();

            if(emprestimosDoItem != null && !emprestimosDoItem.isEmpty ()) {
                for (Emprestimo emprestimoDoItem : emprestimosDoItem) {
                    if(emprestimosAprovadosOuConfirmadosConflitantes.contains (emprestimoDoItem))
                        throw new EmprestimoException ("O item " + item.getNome () + " já possui um empréstimo nessa " +
                                "data e hora!");
                }
            }


        }
    }*/

    @Override
    public int calcularQuantidadeTransacao(Item item, List<Emprestimo> emprestimos) {
        int quantidadeEmprestada = 0;
        for (Emprestimo emprestimo : emprestimos) {
            Integer quantidade = emprestimo.getItensQuantidade().getOrDefault(item, 0);
            quantidadeEmprestada += quantidade;
        }
        return quantidadeEmprestada;
    }


    @Override
    public void verificarLimiteTransacaoEmAnalise (User participante) {

    }

    @Override
    public boolean ehSolicitante (Emprestimo transacao, UsuarioDetails usuarioDetails) {
        return false;
    }

    @Override
    public boolean ehUsuarioAutorizado (Emprestimo transacao, UsuarioDetails usuarioLogado) {
        return false;
    }

    @Override
    public void verificarTransacaoDeMesmaDataDoUsuario (User solicitante, Emprestimo transacao) {

    }

    @Override
    public void deletarItensAssociados (Item item) {
        // Implementar método de deletar itens associados ao empréstimo
    }
}
