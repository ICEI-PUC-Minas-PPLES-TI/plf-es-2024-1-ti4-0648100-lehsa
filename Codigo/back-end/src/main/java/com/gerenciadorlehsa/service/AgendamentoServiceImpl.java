package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.entity.enums.StatusTransacaoItem;
import com.gerenciadorlehsa.exceptions.lancaveis.*;
import com.gerenciadorlehsa.repository.AgendamentoRepository;
import com.gerenciadorlehsa.security.UsuarioDetails;
import com.gerenciadorlehsa.service.interfaces.AgendamentoService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import com.gerenciadorlehsa.util.DataHoraUtil;
import jakarta.transaction.Status;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.gerenciadorlehsa.entity.enums.StatusTransacaoItem.*;
import static com.gerenciadorlehsa.util.ConstantesNumUtil.LIMITE_AGENDAMENTOS_EM_ANALISE;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_SERVICE;
import static java.lang.String.format;
import static org.springframework.beans.BeanUtils.copyProperties;

@Slf4j(topic = AGENDAMENTO_SERVICE)
@Service
public class AgendamentoServiceImpl extends TransacaoService<Agendamento> implements OperacoesCRUDService<Agendamento>, AgendamentoService{

    private final AgendamentoRepository agendamentoRepository;


    @Autowired
    public AgendamentoServiceImpl (ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService, AgendamentoRepository agendamentoRepository) {
        super (validadorAutorizacaoRequisicaoService);
        this.agendamentoRepository = agendamentoRepository;
    }

//----------------CRUD - INÍCIO---------------------------------------

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


    @Override
    @Transactional
    public Agendamento criar (Agendamento obj) {
        log.info(">>> criando: criando agendamento");
        validadorAutorizacaoRequisicaoService.getUsuarioLogado();

        checkTecnicoNaoSolicita (obj);
        LocalDateTime dataHoraInicio = obj.getDataHoraInicio ();
        LocalDateTime dataHoraFim = obj.getDataHoraFim ();

        DataHoraUtil.dataValida (dataHoraInicio, dataHoraFim);

        if(!transacoesAprovadasOuConfirmadasConflitantes(dataHoraInicio, dataHoraFim).isEmpty ())
            throw new AgendamentoException ("Já existe agendamento aprovado pelo administrador ou confirmado pelo " +
                    "usuário" +
                    "para essa data");

        verificarLimiteTransacaoEmAnalise (obj.getSolicitantes ());
        verificarTransacaoDeMesmaDataDoUsuario (obj.getSolicitantes (), obj);

        obj.setId (null);

        return agendamentoRepository.save (obj);
    }

    @Override
    public Agendamento atualizar (Agendamento obj) {
        log.info(">>> atualizar: atualizando agendamento");
        Agendamento agendamentoAtt = encontrarPorId(obj.getId());

        UsuarioDetails usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();


        if (!ehSolicitante(obj, usuarioLogado)) {
            throw new UsuarioNaoAutorizadoException("O usuário não possui permissão para atualizar o agendamento");
        }

        List<String> atributosIguais = atributosIguais(agendamentoAtt, obj);



        LocalDateTime dataHoraInicio = obj.getDataHoraInicio ();
        LocalDateTime dataHoraFim = obj.getDataHoraFim ();

        if (!(atributosIguais.contains("dataHoraInicio") && atributosIguais.contains("dataHoraFim"))) {
            DataHoraUtil.dataValida(dataHoraInicio, dataHoraFim);

            if(!transacoesAprovadasOuConfirmadasConflitantes(dataHoraInicio, dataHoraFim).isEmpty ())
                throw new AgendamentoException ("Já existe agendamento para essa data");

            verificarTransacaoDeMesmaDataDoUsuario(obj.getSolicitantes(), obj);
        }
        atributosIguais.add("tecnico");
        atributosIguais.add("statusTransacaoItem");
        atributosIguais.add("id");
        String[] propriedadesIgnoradas = new String[atributosIguais.size()];
        propriedadesIgnoradas = atributosIguais.toArray(propriedadesIgnoradas);

        copyProperties(obj, agendamentoAtt, propriedadesIgnoradas);
        log.info("Técnico " + (agendamentoAtt.getTecnico()==null));

        log.info("Teste");
        return this.agendamentoRepository.save(agendamentoAtt);
    }

    @Override
    public List<Agendamento> listarTodos () {
        log.info(">>> listarTodos: listando todos agendamentos");
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        return this.agendamentoRepository.findAll();
    }

    @Override
    @Transactional
    public void deletar (UUID id) {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        Agendamento agendamento = encontrarPorId(id);
        deletarAgendamentoDaListaDosUsuarios (agendamento);
        //deletarAgendamentoDaListaDosItens(agendamento);
        log.info(">>> deletar: deletando agendamento");
        try{
            this.agendamentoRepository.deleteById(id);
        } catch (Exception e) {
            throw new DeletarEntidadeException (format("existem entidades relacionadas: %s", e));
        }
    }

//----------------CRUD - FIM ---------------------------------------



//----------------AgendamentoService - INÍCIO ---------------------------

    @Override
    public List<Agendamento> listarAgendamentoUsuario (@NotNull User usuario) {
        log.info(">>> listarAgendamentoUsuario: listando todos agendamentos do usuario de id: " + usuario.getId());
        UsuarioDetails usuarioLogado = validadorAutorizacaoRequisicaoService.getUsuarioLogado();
        log.info(""+usuarioLogado.getId());
        if (usuarioLogado.getId().compareTo(usuario.getId()) == 0 || usuarioLogado.getPerfilUsuario().getCodigo() == 1)
            return this.agendamentoRepository.findBySolicitantes(usuario);

        throw new UsuarioNaoAutorizadoException("O usuário não possui permissão para ver esses agendamentos");
    }

    @Override
    public void atualizarTecnico (User tecnico, @NotNull UUID id) {
        log.info(">>> atualizarTecnico: atualizando tecnico do agendamento");
        Agendamento agendamento = encontrarPorId(id);
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        verificarPerfilTecnico (tecnico);
        agendamento.setTecnico(tecnico);
        this.agendamentoRepository.save(agendamento);
    }

    @Override
    @Transactional
    public void deletarAgendamentoSeVazio(UUID id) {
        encontrarPorId(id);
        log.info(">>> deletar: deletando agendamento sem solicitantes");
        try{
            this.agendamentoRepository.deleteById(id);
        } catch (Exception e) {
            throw new DeletarEntidadeException (format("existem entidades relacionadas: %s", e));
        }
    }


//----------------AgendamentoService - FIM ---------------------------


//----------------TransacaoService - INÍCIO ---------------------------
    @Override
    public void atualizarStatus (@NotNull String status, @NotNull UUID id) {
        log.info(">>> atualizarStatus: atualizando status do agendamento");
        try {
            StatusTransacaoItem statusUpperCase =
                    Enum.valueOf(StatusTransacaoItem.class, status.toUpperCase());

            Agendamento agendamento = encontrarPorId(id);

            if(!agendamentoRepository.findAprovadosOuConfirmadosConflitantes (agendamento.getDataHoraInicio (), agendamento.getDataHoraFim ()).isEmpty () && (statusUpperCase == APROVADO || statusUpperCase == CONFIRMADO)) {
                throw new AgendamentoException ("Um agendamento para essa data já foi aprovado ou confirmado.");
            }


            if (statusUpperCase.equals(StatusTransacaoItem.CANCELADO) ||
                    statusUpperCase.equals(CONFIRMADO)) {
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

    @Override
    public List<Agendamento> transacoesAprovadasOuConfirmadasConflitantes(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        log.info(">>> Verificar conflito de data: barrando agendamento solicitados em uma mesma data de agendamento " +
                "confirmado ou aprovado");
        return agendamentoRepository.findAprovadosOuConfirmadosConflitantes (dataHoraInicio, dataHoraFim);
    }

    @Override
    public void verificarLimiteTransacaoEmAnalise(User participante) {
        long agendamentosEmAnalise = participante.getAgendamentosRealizados ()
                .stream ()
                .filter(agendamento -> agendamento.getStatusTransacaoItem() == EM_ANALISE)
                .count();
        if(agendamentosEmAnalise > LIMITE_AGENDAMENTOS_EM_ANALISE)
            throw new AgendamentoException ("O usuário atingiu o limite de agendamentos em análise");
    }

    @Override
    public void verificarTransacaoDeMesmaDataDoUsuario(User solicitante, Agendamento agendamento) {
        boolean conflitoDeData = solicitante.getAgendamentosRealizados().stream()
                .anyMatch(agendamentoExistente -> temConflitoDeData(agendamentoExistente, agendamento));

        if (conflitoDeData) {
            throw new AgendamentoException ("Um dos solicitantes já fez uma agendamento na mesma data");
        }
    }


    @Override
    public boolean ehUsuarioAutorizado(Agendamento agendamento, UsuarioDetails usuarioLogado) {
        log.info(">>> Verificar autorização do usuário: Verificando se usuário é o técnico, adm ou solicitante do " +
                "agendamento");
        return ehSolicitante(agendamento, usuarioLogado) ||
                ehTecnico(agendamento, usuarioLogado) ||
                usuarioLogado.getPerfilUsuario().getCodigo() == 1;
    }

    @Override
    public boolean ehSolicitante(Agendamento agendamento, UsuarioDetails usuarioLogado) {
        log.info(">>> ehSolicitante: Verificando se o usuário logado é o solicitante do agendamento procurado");
        return agendamento.getSolicitantes().stream()
                .anyMatch(solicitante -> Objects.equals(solicitante.getEmail(), usuarioLogado.getEmail()));
    }

    @Override
    public int calcularQuantidadeTransacao(Item item, List<Agendamento> agendamentos) {
        int quantidadeAgendada = 0;
        for (Agendamento agendamento : agendamentos) {
            Integer quantidade = agendamento.getItensQuantidade().getOrDefault(item, 0);
            quantidadeAgendada += quantidade;
        }
        return quantidadeAgendada;
    }


//----------------TransacaoService - FIM ---------------------------

    private void verificarLimiteTransacaoEmAnalise(List<User> solicitantes) {
        log.info(">>> Verificar limite de solicitação: Barrando limite excedente de solicitação");
        for (User solicitante : solicitantes) {
            verificarLimiteTransacaoEmAnalise (solicitante);
        }
    }

    private void verificarTransacaoDeMesmaDataDoUsuario(List<User> solicitantes, Agendamento agendamento) {
        log.info(">>> Verificar conflito de data de um solicitante: barrando agendamento de mesma data de um solicitante");
        for (User solicitante : solicitantes) {
           verificarTransacaoDeMesmaDataDoUsuario (solicitante, agendamento);
        }
    }


    private boolean ehTecnico(Agendamento agendamento, UsuarioDetails usuarioLogado) {
        log.info(">>> ehTecnico: Verificando se o usuário logado é o técnico do agendamento");
        if (agendamento.getTecnico() == null)
            return false;
        return Objects.equals(agendamento.getTecnico().getEmail(), usuarioLogado.getEmail());
    }


    private void checkTecnicoNaoSolicita(Agendamento agendamento) {
        log.info(">>> Verificar solicitação de técnico: Barrando solicitação de agendamento do técnico");
        if(agendamento.getTecnico () != null)
            if(agendamento.getSolicitantes ().contains (agendamento.getTecnico ()))
                throw new AgendamentoException ("O técnico encarregado não pode ser solicitante");
    }


    private void verificarPerfilTecnico(User tecnico) {
        log.info(">>> Verificando perfil de técnico: barrando usuário que não é técnico");
        if(tecnico != null) {
            if(tecnico.getPerfilUsuario () != 3)
                throw new AgendamentoException ("O usuário encarregado para ser técnico não tem o perfil " +
                        "correspondente");
        }
    }


    private List<String> atributosIguais (Agendamento a, Agendamento b) {
        List<String> atributosIguais = new ArrayList<>();

        if (a.getDataHoraInicio().isEqual(b.getDataHoraInicio()))
            atributosIguais.add("dataHoraInicio");
        if (a.getDataHoraFim().isEqual(b.getDataHoraFim()))
            atributosIguais.add("dataHoraFim");
        if (a.getObservacaoSolicitacao().equals(b.getObservacaoSolicitacao()))
            atributosIguais.add("observacaoSolicitacao");

        return atributosIguais;
    }


    public void deletarAgendamentoDaListaDosUsuarios(Agendamento agendamento) {

        if(agendamento.getSolicitantes () != null && !agendamento.getSolicitantes ().isEmpty ())
            for (User solicitante : agendamento.getSolicitantes()) {
                solicitante.getAgendamentosRealizados ().remove (agendamento);
            }
    }


    //---------------MÉTODOS DO MAPA------------

    public List<Agendamento> findByItem(Item item) {
        return agendamentoRepository.findByItem(item);
    }


  /*  public void deletarAgendamentoDaListaDosItens(Agendamento agendamento) {

        if(agendamento.getItens () != null && !agendamento.getItens ().isEmpty ())
            for (Item item : agendamento.getItens ()) {
                item.getAgendamentos ().remove (agendamento);
            }
    }*/





}
