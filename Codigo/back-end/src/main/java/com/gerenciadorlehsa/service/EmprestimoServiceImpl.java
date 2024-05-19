package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.exceptions.lancaveis.EmprestimoException;
import com.gerenciadorlehsa.repository.EmprestimoRepository;
import com.gerenciadorlehsa.security.UsuarioDetails;
import com.gerenciadorlehsa.service.interfaces.EmprestimoService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import com.gerenciadorlehsa.util.DataHoraUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.gerenciadorlehsa.entity.enums.StatusTransacaoItem.EM_ANALISE;
import static com.gerenciadorlehsa.util.ConstantesNumUtil.LIMITE_EMPRESTIMO_EM_ANALISE;

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
        ehUsuarioAutorizado(obj, validadorAutorizacaoRequisicaoService.getUsuarioLogado ());
        verificarLimiteTransacaoEmAnalise(obj.getSolicitante());

        LocalDateTime dataHoraInicio = obj.getDataHoraInicio ();
        LocalDateTime dataHoraFim = obj.getDataHoraFim ();

        DataHoraUtil.dataValidaEmprestimo (dataHoraInicio, dataHoraFim);
        verificarTransacaoDeMesmaDataDoUsuario(obj.getSolicitante(), obj);

        if(transacoesAprovadasOuConfirmadasConflitantes(dataHoraInicio, dataHoraFim).stream()
                .anyMatch(emprestimo -> emprestimo.getItensQuantidade()
                        .keySet().stream().anyMatch(item -> obj.getItensQuantidade().containsKey(item))))
            throw new EmprestimoException("Algum item já está sendo emprestado entre essas datas!");

        obj.setId(null);
        return emprestimoRepository.save(obj);
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
        long emprestimosEmAnalise = participante.getEmprestimos ()
                .stream ()
                .filter(emprestimo -> emprestimo.getStatusTransacaoItem() == EM_ANALISE)
                .count();

        if(emprestimosEmAnalise > LIMITE_EMPRESTIMO_EM_ANALISE)
            throw new EmprestimoException("O usuário atingiu o limite de emprestimos em análise");
    }

    @Override
    public boolean ehSolicitante (Emprestimo transacao, UsuarioDetails usuarioDetails) {
        return transacao.getSolicitante().getEmail().equals(usuarioDetails.getEmail());
    }

    @Override
    public boolean ehUsuarioAutorizado (Emprestimo transacao, UsuarioDetails usuarioLogado) {
        //ou usario eh o solicitante ou um adm para ser autorizado
        return ehSolicitante(transacao, usuarioLogado) ||
                usuarioLogado.getPerfilUsuario().getCodigo() == 1;
    }

    @Override
    public void verificarTransacaoDeMesmaDataDoUsuario (User solicitante, Emprestimo transacao) {
        boolean conflitoDeData = solicitante.getEmprestimos().stream()
                .anyMatch(emprestimoExistente -> temConflitoDeData(emprestimoExistente, transacao));

        if (conflitoDeData) {
            throw new EmprestimoException ("Um dos solicitantes já fez uma agendamento na mesma data");
        }

    }

    @Override
    public void deletarItensAssociados (Item item) {
        // Implementar método de deletar itens associados ao empréstimo
    }
}
