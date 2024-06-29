package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.*;
import com.gerenciadorlehsa.entity.enums.StatusTransacao;
import com.gerenciadorlehsa.exceptions.lancaveis.*;
import com.gerenciadorlehsa.repository.EmprestimoRepository;
import com.gerenciadorlehsa.security.UserDetailsImpl;
import com.gerenciadorlehsa.service.interfaces.EmprestimoService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import com.gerenciadorlehsa.util.DataHoraUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.gerenciadorlehsa.entity.enums.StatusTransacao.*;
import static com.gerenciadorlehsa.util.ConstantesNumUtil.LIMITE_EMPRESTIMO_EM_ANALISE;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.EMPRESTIMO_SERVICE;
import static com.gerenciadorlehsa.util.DataHoraUtil.dataValidaEmprestimo;
import static java.lang.String.format;

@Slf4j(topic = EMPRESTIMO_SERVICE)
@Service
@Schema(description = "Contém as regras de negócio para concessão de empréstimo de itens do laboratório")
public class EmprestimoServiceImpl extends TransacaoService<Emprestimo, EmprestimoRepository> implements OperacoesCRUDService<Emprestimo>,
        EmprestimoService{

    private final EmprestimoRepository emprestimoRepository;
    private final EnderecoService enderecoService;

    public EmprestimoServiceImpl (ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService, EmprestimoRepository emprestimoRepository, EnderecoService enderecoService) {
        super (validadorAutorizacaoRequisicaoService);
        this.emprestimoRepository = emprestimoRepository;
        this.enderecoService = enderecoService;
    }


    @Override
    protected EmprestimoRepository getTransacaoRepository () {
        return this.emprestimoRepository;
    }

    @Override
    public Emprestimo encontrarPorId (UUID id) {
        UserDetailsImpl usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();
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

        obj.setStatusTransacao (EM_ANALISE);
        obj.setId(null);
        obj.getLocalUso().setId(null);
        return emprestimoRepository.save(obj);
    }

    @Override
    public Emprestimo atualizar (Emprestimo obj) {
        Emprestimo emprestimoExistente = encontrarPorId(obj.getId());

        verificarAutorizacaoDoUsuario (emprestimoExistente);
        validarDataHoraAtt(encontrarAtributosIguais(obj, emprestimoExistente), obj);

        obj.setStatusTransacao (emprestimoExistente.getStatusTransacao ());
        obj.setSolicitante(emprestimoExistente.getSolicitante());

        if (emprestimoRepository.countEmprestimoByLocalUso(emprestimoExistente.getLocalUso()) == 1)
            obj.getLocalUso().setId(emprestimoExistente.getLocalUso().getId());

        return emprestimoRepository.save(obj);
    }

   /* public void atualizarEndereco(Emprestimo newEmprestimo, Emprestimo emprestimoAtt) {
        Endereco enderecoNewEmprestimo = newEmprestimo.getLocalUso ();
        if(!enderecoService.enderecoExiste (enderecoNewEmprestimo)) {
            enderecoNewEmprestimo = enderecoService.criar (enderecoNewEmprestimo);
            emprestimoAtt.setLocalUso (enderecoNewEmprestimo);
        } else if (emprestimoAtt.getLocalUso () != enderecoNewEmprestimo) {
            emprestimoAtt.setLocalUso (enderecoNewEmprestimo);
        }
    }*/


    @Override
    public void deletar (UUID id) {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        Emprestimo obj = encontrarPorId(id);
        try {
            emprestimoRepository.deleteById(id);
            deletarEndereco(obj.getLocalUso());
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
        StatusTransacao statusUpperCase = getStatusUpperCase(status);

        Emprestimo emprestimo = encontrarPorId (id);

        verificarAutorizacaoDoUsuario(emprestimo, statusUpperCase);

        verificarConflitosDeTransacaoAPROVADOeCONFIRMADO(emprestimo, statusUpperCase);

        verificarCondicoesDeConfirmacao (emprestimo, statusUpperCase);

        verificarCondicoesDeAprovacao (emprestimo, statusUpperCase);

        emprestimo.setStatusTransacao (statusUpperCase);
        emprestimoRepository.save(emprestimo);
    }



    @Override
    public void verificarLimiteTransacaoEmAnalise (User participante) {
        long emprestimosEmAnalise = participante.getEmprestimos ()
                .stream ()
                .filter(emprestimo -> emprestimo.getStatusTransacao () == EM_ANALISE)
                .count();

        if(emprestimosEmAnalise > LIMITE_EMPRESTIMO_EM_ANALISE)
            throw new EmprestimoException("O usuário atingiu o limite de emprestimos em análise");
    }

    @Override
    public boolean ehSolicitante (Emprestimo transacao, UserDetailsImpl userDetailsImpl) {
        return transacao.getSolicitante().getEmail().equals(userDetailsImpl.getEmail());
    }

    @Override
    public boolean ehUsuarioAutorizado (Emprestimo transacao, UserDetailsImpl usuarioLogado) {
        //ou usario eh o solicitante ou um adm para ser autorizado
        return ehSolicitante(transacao, usuarioLogado) ||
                usuarioLogado.getPerfilUsuario().getCodigo() == 1;
    }

    @Override
    public void verificarTransacaoDeMesmaDataDoUsuario (User solicitante, Emprestimo transacao) {
        boolean conflitoDeData = solicitante.getEmprestimos().stream()
                .anyMatch(emprestimoExistente -> temConflitoDeData(emprestimoExistente, transacao));

        if (conflitoDeData) {
            throw new EmprestimoException ("O solicitente já fez uma emprestimo na mesma data");
        }

    }



    @Override
    public void deletarEmprestimoDaListaDoUsuario(Emprestimo emprestimo) {
        if(emprestimo.getSolicitante () != null)
            emprestimo.getSolicitante().getEmprestimos().remove (emprestimo);
    }

    @Override
    public void verificarConflitosDeEmprestimo(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        if (!transacoesAprovadasOuConfirmadasConflitantes(dataHoraInicio, dataHoraFim).isEmpty()) {
            throw new EmprestimoException("Já existe emprestimo ou agendamento aprovado pelo administrador ou confirmado pelo usuário para essa data");
        }
    }

    @Override
    public void validarDataHoraAtt(List<String> atributosIguais, Emprestimo obj){
        LocalDateTime dataHoraInicio = obj.getDataHoraInicio ();
        LocalDateTime dataHoraFim = obj.getDataHoraFim ();
        if (!atributosIguais.contains("dataHoraInicio") || !atributosIguais.contains("dataHoraFim")) {
            dataValidaEmprestimo(dataHoraInicio, dataHoraFim);
            List<Emprestimo> conflitantes = transacoesAprovadasOuConfirmadasConflitantes(dataHoraInicio, dataHoraFim);
            if (!conflitantes.isEmpty()) {
                if (conflitantes.stream().noneMatch(emprestimo -> emprestimo.getId().equals(obj.getId()))) {
                    throw new EmprestimoException("Já existe emprestimo para essa data");
                }
            }
            verificarTransacaoDeMesmaDataDoUsuario(obj.getSolicitante(), obj);
        }
    }

    @Override
    public void deletarEndereco (Endereco endereco) {
        int nEmprestimosComEndereco = emprestimoRepository.countEmprestimoByLocalUso(endereco);

        if (nEmprestimosComEndereco == 1 || nEmprestimosComEndereco == 0) {
            enderecoService.deletar(endereco.getId());
        }
    }
}
