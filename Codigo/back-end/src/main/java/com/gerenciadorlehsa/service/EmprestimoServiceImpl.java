package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Emprestimo;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.entity.enums.StatusTransacaoItem;
import com.gerenciadorlehsa.exceptions.lancaveis.*;
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

import static com.gerenciadorlehsa.entity.enums.StatusTransacaoItem.*;
import static com.gerenciadorlehsa.util.ConstantesNumUtil.LIMITE_EMPRESTIMO_EM_ANALISE;
import static java.lang.String.format;

@Service
public class EmprestimoServiceImpl extends TransacaoService<Emprestimo> implements OperacoesCRUDService<Emprestimo>, EmprestimoService{

    private final EmprestimoRepository emprestimoRepository;
    private final EnderecoService enderecoService;

    @Autowired
    public EmprestimoServiceImpl (ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService, EmprestimoRepository emprestimoRepository,
                                  EnderecoService enderecoService) {
        super (validadorAutorizacaoRequisicaoService);
        this.emprestimoRepository = emprestimoRepository;
        this.enderecoService = enderecoService;
    }

    @Override
    public Emprestimo encontrarPorId (UUID id) {
        UsuarioDetails usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();
        Emprestimo obj = emprestimoRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException(
                format("Emprestimo não encontrado, id: %s", id)));

        if(!ehUsuarioAutorizado(obj, usuarioLogado))
            throw new UsuarioNaoAutorizadoException("O usuário não possui permissão para acessar o emprestimo");
        return obj;
    }

    @Override
    public Emprestimo criar (Emprestimo obj) {
        ehUsuarioAutorizado(obj, validadorAutorizacaoRequisicaoService.getUsuarioLogado ());
        if (obj.getItensQuantidade().keySet().stream().anyMatch(item -> !item.getEmprestavel()))
            throw new EmprestimoException("Item não emprestavel selecionado para empresitmo!");
        verificarLimiteTransacaoEmAnalise(obj.getSolicitante());

        LocalDateTime dataHoraInicio = obj.getDataHoraInicio ();
        LocalDateTime dataHoraFim = obj.getDataHoraFim ();

        DataHoraUtil.dataValidaEmprestimo (dataHoraInicio, dataHoraFim);
        verificarConflitosDeEmprestimo(dataHoraInicio, dataHoraFim);
        verificarTransacaoDeMesmaDataDoUsuario(obj.getSolicitante(), obj);

        obj.setStatusTransacaoItem(EM_ANALISE);
        obj.setId(null);
        obj.getLocalUso().setId(null);
        return emprestimoRepository.save(obj);
    }

    @Override
    public Emprestimo atualizar (Emprestimo obj) {
        return null;
    }

    @Override
    public void deletar (UUID id) {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        Emprestimo obj = encontrarPorId(id);
        int nEmprestimosComEndereco = emprestimoRepository.countEmprestimoByLocalUso(obj.getLocalUso());
        try {
            emprestimoRepository.deleteById(id);
            if (nEmprestimosComEndereco == 1) {
                enderecoService.deletar(obj.getLocalUso().getId());
            }
            deletarEmprestimoDaListaDoUsuario(obj);
        } catch (Exception e) {
            throw new DeletarEntidadeException(format("existem entidades relacionadas: %s", e));
        }
    }

    @Override
    public List<Emprestimo> listarTodos () {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        return this.emprestimoRepository.findAll();
    }

    @Override
    public void atualizarStatus (String status, UUID id) {

    }

    @Override
    public List<Emprestimo> transacoesAprovadasOuConfirmadasConflitantes (LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        return emprestimoRepository.findAprovadosOuConfirmadosConflitantes (dataHoraInicio, dataHoraFim);
    }



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
        List<Emprestimo> emprestimos = emprestimoRepository.findByItem(item);

        if(emprestimos != null && !emprestimos.isEmpty ()) {
            for (Emprestimo emprestimo : emprestimos) {
                emprestimo.getItensQuantidade().remove(item);
                emprestimoRepository.save(emprestimo);
            }
        }
    }

    @Override
    public void verificarConflitosDeTransacaoAPROVADOeCONFIRMADO (Emprestimo transacao, StatusTransacaoItem status) {
        if (!transacoesAprovadasOuConfirmadasConflitantes(transacao.getDataHoraInicio(), transacao.getDataHoraFim()).isEmpty()
                && (status == APROVADO || status == CONFIRMADO)) {
            throw new AgendamentoException("Um agendamento para essa data já foi aprovado ou confirmado.");
        }
    }

    @Override
    public void copiarAtributosRelevantes (Agendamento source, Agendamento target, List<String> atributosIguais) {

    }

    private void deletarEmprestimoDaListaDoUsuario(Emprestimo emprestimo) {
        if(emprestimo.getSolicitante () != null)
            emprestimo.getSolicitante().getEmprestimos().remove (emprestimo);
    }

    private void verificarConflitosDeEmprestimo(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        if (!transacoesAprovadasOuConfirmadasConflitantes(dataHoraInicio, dataHoraFim).isEmpty()) {
            throw new EmprestimoException("Já existe emprestimo ou agendamento aprovado pelo administrador ou confirmado pelo usuário para essa data");
        }
    }

}
