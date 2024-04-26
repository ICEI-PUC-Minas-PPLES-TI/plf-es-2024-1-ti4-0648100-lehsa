package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.entity.enums.StatusTransacaoItem;
import com.gerenciadorlehsa.exceptions.lancaveis.AgendamentoException;
import com.gerenciadorlehsa.exceptions.lancaveis.DataConflitanteAgendamentoException;
import com.gerenciadorlehsa.exceptions.lancaveis.EntidadeNaoEncontradaException;
import com.gerenciadorlehsa.exceptions.lancaveis.UsuarioNaoAutorizadoException;
import com.gerenciadorlehsa.repository.AgendamentoRepository;
import com.gerenciadorlehsa.security.UsuarioDetails;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import com.gerenciadorlehsa.util.DataHoraUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import static com.gerenciadorlehsa.entity.enums.StatusTransacaoItem.EM_ANALISE;
import static com.gerenciadorlehsa.util.ConstantesNumUtil.LIMITE_AGENDAMENTOS_EM_ANALISE;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_SERVICE;
import static org.springframework.beans.BeanUtils.copyProperties;

@Slf4j(topic = AGENDAMENTO_SERVICE)
@Service
@AllArgsConstructor
public class AgendamentoServiceImpl implements OperacoesCRUDService<Agendamento> {


    private final AgendamentoRepository agendamentoRepository;

    private final ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;


    /**
     * Procura um agendamento por id
     * @param id o id do agendamento
     * @return objeto agendamento cujo id foi o passado como argumento
     */
    @Override
    public Agendamento encontrarPorId(UUID id) {
        log.info(">>> encontrarPorId: encontrando agendamento por id");

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format("Agendamento não encontrado, id: %s", id)));

        UsuarioDetails usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();

        if (!ehUsuarioAutorizado(agendamento, usuarioLogado)) {
            throw new UsuarioNaoAutorizadoException("O usuário não possui permissão para acessar o agendamento");
        }

        return agendamento;
    }


//Você já tem um agendamento para essa data
    @Override
    public Agendamento criar (Agendamento obj) {
        validadorAutorizacaoRequisicaoService.getUsuarioLogado();
        verificarTecnicoAgendamento(obj);
        LocalDateTime dataHoraInicio = obj.getDataHoraInicio ();
        LocalDateTime dataHoraFim = obj.getDataHoraFim ();

        DataHoraUtil.dataValida (dataHoraInicio, dataHoraFim);
        verificarConflitoData (dataHoraInicio, dataHoraFim);
        verificarLimiteAgendamentosEmAnaliseDosParticipantes (obj.getSolicitantes ());
        verificarAgendamentosDeMesmaDataDoUsuario (obj.getSolicitantes (), obj);

        obj.setId (null);
        obj.setStatusTransacaoItem (EM_ANALISE);

        return agendamentoRepository.save (obj);
    }

    // nao atualiza o status
    @Override
    public Agendamento atualizar (Agendamento obj) {
        log.info(">>> atualizar: atualizando agendamento");
        Agendamento agendamentoAtt = encontrarPorId(obj.getId());

        verificarTecnicoAgendamento(obj);

        LocalDateTime dataHoraInicio = obj.getDataHoraInicio ();
        LocalDateTime dataHoraFim = obj.getDataHoraFim ();

        DataHoraUtil.dataValida (dataHoraInicio, dataHoraFim);
        verificarConflitoData (dataHoraInicio, dataHoraFim);
        verificarAgendamentosDeMesmaDataDoUsuario (obj.getSolicitantes (), obj);

        String[] propriedadesIgnoradas = new String[]{"id", "statusTransacaoItem"};

        copyProperties(obj, agendamentoAtt, propriedadesIgnoradas);
        return this.agendamentoRepository.save(agendamentoAtt);
    }

    @Override
    public void deletar (UUID id) {

    }

    @Override
    public List<Agendamento> listarTodos () {
        log.info(">>> listarTodos: listando todos agendamentos");
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        return this.agendamentoRepository.findAll();
    }


    private void verificarTecnicoAgendamento(Agendamento agendamento) {

        if(agendamento.getTecnico () != null) {
            if(agendamento.getTecnico ().getPerfilUsuario () != 3)
                throw new AgendamentoException ("O usuário encarregado para ser técnico não tem o perfil " +
                        "correspondente");
        }
    }


    private void verificarAgendamentosDeMesmaDataDoUsuario(List<User> solicitantes, Agendamento agendamento) {

        for (User solicitante : solicitantes) {
            boolean conflitoDeData = solicitante.getAgendamentosRealizados().stream()
                    .anyMatch(agendamentoExistente -> temConflitoDeData(agendamentoExistente, agendamento));

            if (conflitoDeData) {
                throw new AgendamentoException ("Um dos solicitantes já fez uma agendamento na mesma data");
            }
        }
    }

    // Método para verificar se há conflito de datas entre dois agendamentos
    private boolean temConflitoDeData(Agendamento agendamentoExistente, Agendamento novoAgendamento) {
        LocalDateTime dataHoraInicioExistente = agendamentoExistente.getDataHoraInicio();
        LocalDateTime dataHoraFimExistente = agendamentoExistente.getDataHoraFim();
        LocalDateTime dataHoraInicioNovo = novoAgendamento.getDataHoraInicio();
        LocalDateTime dataHoraFimNovo = novoAgendamento.getDataHoraFim();

        return (dataHoraInicioNovo.isBefore(dataHoraFimExistente) ||
                dataHoraInicioNovo.isEqual(dataHoraFimExistente)) &&
                (dataHoraFimNovo.isAfter(dataHoraInicioExistente) ||
                        dataHoraFimNovo.isEqual(dataHoraInicioExistente));
    }



    private void verificarLimiteAgendamentosEmAnaliseDosParticipantes(List<User> solicitantes) {

        long agendamentosEmAnalise;

        for (User solicitante : solicitantes) {

            agendamentosEmAnalise = solicitante.getAgendamentosRealizados().stream()
                    .filter(agendamento -> agendamento.getStatusTransacaoItem() == StatusTransacaoItem.EM_ANALISE)
                    .count();

            if(agendamentosEmAnalise > LIMITE_AGENDAMENTOS_EM_ANALISE)
                throw new AgendamentoException ("O usuário atingiu o limite de agendamentos em análise");
        }

    }


    public void verificarConflitoData(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {

        List<Agendamento> agendamentosConflitantes =
                agendamentoRepository.findAprovadosOuConfirmadosConflitantes (dataHoraInicio, dataHoraFim);

        if(!agendamentosConflitantes.isEmpty ())
            throw new DataConflitanteAgendamentoException ("Já existe um agendamento para essa data");
    }


    /**
     * Verifica se o usuário logado é autorizado para acessar o agendamento.
     *
     * @param agendamento   Agendamento
     * @param usuarioLogado Usuário logado
     * @return true se o usuário é autorizado, false caso contrário
     */
    private boolean ehUsuarioAutorizado(Agendamento agendamento, UsuarioDetails usuarioLogado) {
        // Verificar se o usuário é solicitante, técnico ou administrador
        return ehSolicitante(agendamento, usuarioLogado) ||
                ehTecnico(agendamento, usuarioLogado) ||
                usuarioLogado.getPerfilUsuario().getCodigo() == 1;
    }


    /**
     * Verifica se o usuário logado é solicitante do agendamento.
     *
     * @param agendamento   Agendamento
     * @param usuarioLogado Usuário logado
     * @return true se o usuário é solicitante, false caso contrário
     */
    private boolean ehSolicitante(Agendamento agendamento, UsuarioDetails usuarioLogado) {
        return agendamento.getSolicitantes().stream()
                .anyMatch(solicitante -> Objects.equals(solicitante.getEmail(), usuarioLogado.getEmail()));
    }

    /**
     * Verifica se o usuário logado é técnico do agendamento.
     *
     * @param agendamento   Agendamento
     * @param usuarioLogado Usuário logado
     * @return true se o usuário é técnico, false caso contrário
     */
    private boolean ehTecnico(Agendamento agendamento, UsuarioDetails usuarioLogado) {
        return Objects.equals(agendamento.getTecnico().getEmail(), usuarioLogado.getEmail());
    }





   /* public void excluirAgendamentoSeSemSolicitantes(UUID agendamentoId) {
        Agendamento agendamento = encontrarPorId (agendamentoId);
        if(!agendamento.getSolicitantes().isEmpty())
            throw new DataIntegrityViolationException ()


        if (agendamento.getSolicitantes().isEmpty()) {
            agendamentoRepository.delete (agendamento);
        } else {
            throw new RuntimeException("Não é possível excluir o agendamento, pois possui solicitantes vinculados.");
        }
    }*/


}
