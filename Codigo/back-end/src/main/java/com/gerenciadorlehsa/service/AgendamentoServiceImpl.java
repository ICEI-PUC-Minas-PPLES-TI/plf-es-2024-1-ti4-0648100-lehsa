package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.entity.enums.StatusTransacaoItem;
import com.gerenciadorlehsa.exceptions.lancaveis.AgendamentoException;
import com.gerenciadorlehsa.exceptions.lancaveis.DataConflitanteAgendamentoException;
import com.gerenciadorlehsa.exceptions.lancaveis.EntidadeNaoEncontradaException;
import com.gerenciadorlehsa.exceptions.lancaveis.UsuarioNaoAutorizadoException;
import com.gerenciadorlehsa.exceptions.lancaveis.EnumNaoEncontradoException;
import com.gerenciadorlehsa.repository.AgendamentoRepository;
import com.gerenciadorlehsa.security.UsuarioDetails;
import com.gerenciadorlehsa.service.interfaces.AgendamentoService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import com.gerenciadorlehsa.util.DataHoraUtil;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class AgendamentoServiceImpl implements OperacoesCRUDService<Agendamento>, AgendamentoService {


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

        UsuarioDetails usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format("Agendamento não encontrado, id: %s", id)));

        if (!ehUsuarioAutorizado(agendamento, usuarioLogado)) {
            throw new UsuarioNaoAutorizadoException("O usuário não possui permissão para acessar o agendamento");
        }

        return agendamento;
    }


//Você já tem um agendamento para essa data
    @Override
    public Agendamento criar (Agendamento obj) {
        log.info(">>> criando: criando agendamento");
        validadorAutorizacaoRequisicaoService.getUsuarioLogado();
        verificarTecnicoAgendamento(obj);
        checkTecnicoNaoSolicita (obj);
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

    private void checkTecnicoNaoSolicita(Agendamento agendamento) {
        log.info(">>> Verificar solicitação de técnico: Barrando solicitação de agendamento do técnico");
        if(agendamento.getTecnico () != null)
            if(agendamento.getSolicitantes ().contains (agendamento.getTecnico ()))
                throw new AgendamentoException ("O técnico encarregado não pode ser solicitante");
    }


    // nao atualiza o status
    @Override
    public Agendamento atualizar (Agendamento obj) {
        log.info(">>> atualizar: atualizando agendamento");
        Agendamento agendamentoAtt = encontrarPorId(obj.getId());

        UsuarioDetails usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();


        if (!ehUsuarioAutorizado(agendamentoAtt, usuarioLogado)) {
            throw new UsuarioNaoAutorizadoException("O usuário não possui permissão para atualizar o agendamento");
        }

        List<String> atributosIguais = atributosIguais(agendamentoAtt, obj);

        if(!atributosIguais.contains("tecnico"))
            verificarTecnicoAgendamento(obj);

        LocalDateTime dataHoraInicio = obj.getDataHoraInicio ();
        LocalDateTime dataHoraFim = obj.getDataHoraFim ();

        if (!(atributosIguais.contains("dataHoraInicio") && atributosIguais.contains("dataHoraFim"))) {
            DataHoraUtil.dataValida(dataHoraInicio, dataHoraFim);
            verificarConflitoData(dataHoraInicio, dataHoraFim);
            verificarAgendamentosDeMesmaDataDoUsuario(obj.getSolicitantes(), obj);
        }
        atributosIguais.add("statusTransacaoItem");
        atributosIguais.add("id");
        String[] propriedadesIgnoradas = new String[atributosIguais.size()];
        propriedadesIgnoradas = atributosIguais.toArray(propriedadesIgnoradas);

        copyProperties(obj, agendamentoAtt, propriedadesIgnoradas);
        return this.agendamentoRepository.save(agendamentoAtt);
    }

    @Override
    public void deletar (UUID id) {
        /*
        //Codigo onde todos do agendamento podem deletar (Adimns, Participantes e tecnico do agendamento)
        Agendamento agendamento = encontrarPorId(id);
        UsuarioDetails usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();

        if (!ehUsuarioAutorizado(agendamento, usuarioLogado)) {
            throw new UsuarioNaoAutorizadoException("O usuário não possui permissão para deletar o agendamento");
        }
        */
        //Somente Admins podem apagar
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        encontrarPorId(id);
        log.info(">>> deletar: deletando agendamento");
        try{
            this.agendamentoRepository.deleteById(id);
        } catch (Exception e){
            // como se comporta?? os outros registros vao se apagar?
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Agendamento> listarTodos () {
        log.info(">>> listarTodos: listando todos agendamentos");
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        return this.agendamentoRepository.findAll();
    }

    @Override
    public void atualizarStatus (@NotNull String status, @NotNull UUID id) {
        log.info(">>> atualizarStatus: atualizando status do agendamento");
        try {
            //Pegando uma string e vendo se tem o tipo correspondente
            StatusTransacaoItem statusUpperCase =
                    Enum.valueOf(StatusTransacaoItem.class, status.toUpperCase());

            Agendamento agendamento = encontrarPorId(id);

            if (statusUpperCase.equals(StatusTransacaoItem.CANCELADO) ||
                    statusUpperCase.equals(StatusTransacaoItem.CONFIRMADO)) {
                UsuarioDetails usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();
                if (!ehUsuarioAutorizado(agendamento, usuarioLogado)) {
                    throw new UsuarioNaoAutorizadoException("O usuário não possui permissão para atualizar o agendamento");
                }
            } else
                validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();

            agendamento.setStatusTransacaoItem(statusUpperCase);
            this.agendamentoRepository.save(agendamento);
            log.info(">>> atualizarStatus: status do agendamento "+agendamento.getId()
                    + " atualizado para "+agendamento.getStatusTransacaoItem());

        } catch (IllegalArgumentException e) {
            throw new EnumNaoEncontradoException ("O status passado não existe: " + status);
        }
    }


    private void verificarTecnicoAgendamento(Agendamento agendamento) {
        log.info(">>> Verificando perfil de técnico: barrando usuário que não é técnico");
        if(agendamento.getTecnico () != null) {
            if(agendamento.getTecnico ().getPerfilUsuario () != 3)
                throw new AgendamentoException ("O usuário encarregado para ser técnico não tem o perfil " +
                        "correspondente");
        }
    }


    private void verificarAgendamentosDeMesmaDataDoUsuario(List<User> solicitantes, Agendamento agendamento) {
        log.info(">>> Verificar conflito de data de um solicitante: barrando agendamento de mesma data de um solicitante");
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
        log.info(">>> Verificando datas conflitantes: barrando agendamento solicitado em uma mesma data");

        if (agendamentoExistente.getId() == novoAgendamento.getId())
            return false;

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
        log.info(">>> Verificar limite de solicitação: Barrando limite excedente de solicitação");
        long agendamentosEmAnalise;

        for (User solicitante : solicitantes) {

            agendamentosEmAnalise = solicitante.getAgendamentosRealizados().stream()
                    .filter(agendamento -> agendamento.getStatusTransacaoItem() == EM_ANALISE)
                    .count();

            if(agendamentosEmAnalise > LIMITE_AGENDAMENTOS_EM_ANALISE)
                throw new AgendamentoException ("O usuário atingiu o limite de agendamentos em análise");
        }

    }


    public void verificarConflitoData(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        log.info(">>> Verificar conflito de data: barrando agendamento solicitados em uma mesma data de agendamento " +
                "confirmado ou aprovado");
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
        log.info(">>> Verificar autorização do usuário: Verificando se usuário é o técnico, adm ou solicitante do " +
                "agendamento");
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
        log.info(">>> ehSolicitante: Verificando se o usuário logado é o solicitante do agendamento procurado");
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
        log.info(">>> ehTecnico: Verificando se o usuário logado é o técnico do agendamento");
        if (agendamento.getTecnico() == null)
            return false;
        return Objects.equals(agendamento.getTecnico().getEmail(), usuarioLogado.getEmail());
    }

    private List<String> atributosIguais (Agendamento a, Agendamento b) {
        List<String> atributosIguais = new ArrayList<>();

        if (a.getDataHoraInicio().isEqual(b.getDataHoraInicio()))
            atributosIguais.add("dataHoraInicio");
        if (a.getDataHoraFim().isEqual(b.getDataHoraFim()))
            atributosIguais.add("dataHoraFim");
        if (a.getObservacaoSolicitacao().equals(b.getObservacaoSolicitacao()))
            atributosIguais.add("observacaoSolicitacao");
        if (a.getTecnico() == (b.getTecnico()))
            atributosIguais.add("tecnico");

        return atributosIguais;
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
