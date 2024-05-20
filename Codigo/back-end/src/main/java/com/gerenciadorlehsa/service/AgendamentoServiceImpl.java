package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import com.gerenciadorlehsa.entity.Professor;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.entity.enums.StatusTransacaoItem;
import com.gerenciadorlehsa.exceptions.lancaveis.*;
import com.gerenciadorlehsa.repository.AgendamentoRepository;
import com.gerenciadorlehsa.security.UsuarioDetails;
import com.gerenciadorlehsa.service.interfaces.AgendamentoService;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import com.gerenciadorlehsa.util.DataHoraUtil;
import com.gerenciadorlehsa.util.EstilizacaoEmailUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.gerenciadorlehsa.entity.enums.StatusTransacaoItem.*;
import static com.gerenciadorlehsa.util.ConstantesNumUtil.LIMITE_AGENDAMENTOS_EM_ANALISE;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_SERVICE;
import static com.gerenciadorlehsa.util.DataHoraUtil.dataValida;
import static java.lang.String.format;
import static org.springframework.beans.BeanUtils.copyProperties;

@Slf4j(topic = AGENDAMENTO_SERVICE)
@Service
public class AgendamentoServiceImpl extends TransacaoService<Agendamento> implements OperacoesCRUDService<Agendamento>, AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    private final MensagemEmailService mensagemEmailService;


    @Autowired
    public AgendamentoServiceImpl (ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService,
                                   AgendamentoRepository agendamentoRepository, MensagemEmailService mensagemEmailService) {
        super (validadorAutorizacaoRequisicaoService);
        this.agendamentoRepository = agendamentoRepository;
        this.mensagemEmailService = mensagemEmailService;
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

        verificarConfirmacaoCadastroProfessor (obj);

        //Enviar e-mail confirmação:
        //enviarEmailParaProfessor (obj);
        //Caso não envie e-mail:
        //obj.setStatusTransacaoItem (EM_ANALISE);

        checkTecnicoNaoSolicita (obj);

        dataValida (obj.getDataHoraInicio (), obj.getDataHoraFim ());

        verificarConflitosDeAgendamento(obj.getDataHoraInicio (), obj.getDataHoraFim ());

        verificarLimiteTransacaoEmAnalise (obj.getSolicitantes ());

        verificarTransacaoDeMesmaDataDoUsuario (obj.getSolicitantes (), obj);
        obj.setId (null);

        verificarTransacaoDeMesmaDataDoProfessor(obj.getProfessor (), obj);


        return agendamentoRepository.save (obj);
    }

    @Override
    public Agendamento atualizar(Agendamento obj) {
        log.info(">>> atualizar: atualizando agendamento");

        Agendamento agendamentoAtt = encontrarPorId(obj.getId());

        verificarNovoProfessor (obj, agendamentoAtt);

        verificarAutorizacaoDoUsuario(agendamentoAtt);

        List<String> atributosIguais = encontrarAtributosIguais(agendamentoAtt, obj);

        validarDataHora(atributosIguais, obj);

        copiarAtributosRelevantes(obj, agendamentoAtt, atributosIguais);

        return agendamentoRepository.save(agendamentoAtt);
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
    public void verificarConfirmacaoCadastroProfessor(Agendamento agendamento) {
        if(!agendamento.getProfessor ().getConfirmaCadastro ())
            throw new ProfessorConfirmaCadastroException ("O Professor ainda não confirmou cadastro");
    }

    @Override
    public void enviarEmailParaProfessor(Agendamento agendamento) {

       /* String linkConfirmacao = ".../agendamento/professor-confirma?id=" + agendamento.getProfessor ().getId ();*/

        String linkConfirmacao = "https://www.youtube.com/watch?v=1bMOsJcigIw&t=10162s";

        String email = agendamento.getProfessor ().getEmail();
        try {
            mensagemEmailService.enviarEmailConfirmacaoAgendamento(email,
                    EstilizacaoEmailUtil.estilizaConfirmacaoAgendamento (linkConfirmacao,
                            agendamento.getDataHoraInicio (), agendamento.getDataHoraFim ()));
        } catch (Exception e) {
            throw new MensagemEmailException ("Envio de e-mail falhou.");
        }
    }


    @Override
    public Agendamento professorConfirmaAgendamento(UUID id) {
        Agendamento agendamento = encontrarPorId (id);
        log.info(">>> professorConfirmaAgendamento: professor confirma agendamento");
        agendamento.setStatusTransacaoItem (EM_ANALISE);
        return agendamentoRepository.save (agendamento);
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

    @Override
    public List<Object[]> listarDatasOcupadas () {
        log.info(">>> listarDatasOcupadas: listando datas ocupadas de agendamento");
        return this.agendamentoRepository.findDataHoraInicioAndFim();
    }

    @Override
    public boolean ehTecnico(Agendamento agendamento, UsuarioDetails usuarioLogado) {
        log.info(">>> ehTecnico: Verificando se o usuário logado é o técnico do agendamento");
        if (agendamento.getTecnico() == null)
            return false;
        return Objects.equals(agendamento.getTecnico().getEmail(), usuarioLogado.getEmail());
    }

    @Override
    public void checkTecnicoNaoSolicita(Agendamento agendamento) {
        log.info(">>> Verificar solicitação de técnico: Barrando solicitação de agendamento do técnico");
        if(agendamento.getTecnico () != null)
            if(agendamento.getSolicitantes ().contains (agendamento.getTecnico ()))
                throw new AgendamentoException ("O técnico encarregado não pode ser solicitante");
    }


    @Override
    public void verificarPerfilTecnico(User tecnico) {
        log.info(">>> Verificando perfil de técnico: barrando usuário que não é técnico");
        if(tecnico != null) {
            if(tecnico.getPerfilUsuario () != 3)
                throw new AgendamentoException ("O usuário encarregado para ser técnico não tem o perfil " +
                        "correspondente");
        }
    }

    @Override
    public Agendamento verificarNovoProfessor(Agendamento novoAgedamento, Agendamento velhoAgendamento) {
        if(novoAgedamento.getProfessor () != velhoAgendamento.getProfessor ()) {
            verificarMudancaProfessor (novoAgedamento);
            velhoAgendamento.setStatusTransacaoItem (AGUARDANDO_CONFIRMACAO_PROFESSOR);
            //enviarEmailParaProfessor (velhoAgendamento);
        }
        return velhoAgendamento;
    }

    @Override
    public void verificarTransacaoDeMesmaDataDoProfessor(Professor professor, Agendamento agendamento) {
        log.info (">>> Verificar conflito de data do professor: barrando agendamento de mesma data de um professor");

        boolean conflitoDeData = professor.getAgendamentos ()
                .stream ()
                .anyMatch (agendamentoAVista -> temConflitoDeData (agendamentoAVista, agendamento));

        if (conflitoDeData) {
            throw new AgendamentoException ("O Professor tem um agendamento marcado pra essa data");
        }
    }

//----------------AgendamentoService - FIM ---------------------------


//----------------TransacaoService - INÍCIO ---------------------------
@Override
public void atualizarStatus(@NotNull String status, @NotNull UUID id) {
    log.info(">>> atualizarStatus: atualizando status do agendamento");
    StatusTransacaoItem statusUpperCase = getStatusUpperCase(status);

    Agendamento agendamento = encontrarPorId (id);

    verificarConflitosDeTransacaoAPROVADOeCONFIRMADO(agendamento, statusUpperCase);

    verificarAutorizacaoDoUsuario(agendamento, statusUpperCase);

    agendamento.setStatusTransacaoItem(statusUpperCase);
    agendamentoRepository.save(agendamento);
    log.info(">>> atualizarStatus: status do agendamento " + agendamento.getId() +
            " atualizado para " + agendamento.getStatusTransacaoItem());
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

    @Override
    public void deletarItensAssociados(Item item) {
        List<Agendamento> agendamentos = agendamentoRepository.findByItem(item);

        if(agendamentos != null && !agendamentos.isEmpty ()) {
            for (Agendamento agendamento : agendamentos) {
                agendamento.getItensQuantidade().remove(item);
                agendamentoRepository.save(agendamento);
            }
        }
    }

    @Override
    public void verificarConflitosDeTransacaoAPROVADOeCONFIRMADO(Agendamento agendamento, StatusTransacaoItem status) {
        if (!agendamentoRepository.findAprovadosOuConfirmadosConflitantes(agendamento.getDataHoraInicio(), agendamento.getDataHoraFim()).isEmpty()
                && (status == APROVADO || status == CONFIRMADO)) {
            throw new AgendamentoException("Um agendamento para essa data já foi aprovado ou confirmado.");
        }
    }

    @Override
    public void copiarAtributosRelevantes(Agendamento source, Agendamento target, List<String> atributosIguais) {
        atributosIguais.add("tecnico");
        atributosIguais.add("statusTransacaoItem");
        atributosIguais.add("id");
        String[] propriedadesIgnoradas = atributosIguais.toArray(new String[0]);
        copyProperties(source, target, propriedadesIgnoradas);
    }


//----------------TransacaoService - FIM ---------------------------



    private void verificarTransacaoDeMesmaDataDoUsuario(List<User> solicitantes, Agendamento agendamento) {
        log.info(">>> Verificar conflito de data de um solicitante: barrando agendamento de mesma data de um solicitante");
        for (User solicitante : solicitantes) {
            verificarTransacaoDeMesmaDataDoUsuario (solicitante, agendamento);
        }
    }


    private void verificarLimiteTransacaoEmAnalise(List<User> solicitantes) {
        log.info(">>> Verificar limite de solicitação: Barrando limite excedente de solicitação");
        for (User solicitante : solicitantes) {
            verificarLimiteTransacaoEmAnalise (solicitante);
        }
    }


    public void deletarAgendamentoDaListaDosUsuarios(Agendamento agendamento) {

        if(agendamento.getSolicitantes () != null && !agendamento.getSolicitantes ().isEmpty ())
            for (User solicitante : agendamento.getSolicitantes()) {
                solicitante.getAgendamentosRealizados ().remove (agendamento);
            }
    }


    private void validarDataHora(List<String> atributosIguais, Agendamento obj){
        LocalDateTime dataHoraInicio = obj.getDataHoraInicio ();
        LocalDateTime dataHoraFim = obj.getDataHoraFim ();
        if (!atributosIguais.contains("dataHoraInicio") || !atributosIguais.contains("dataHoraFim")) {
            dataValida(dataHoraInicio, dataHoraFim);
            if (!transacoesAprovadasOuConfirmadasConflitantes(dataHoraInicio, dataHoraFim).isEmpty())
                throw new AgendamentoException("Já existe agendamento para essa data");
            verificarTransacaoDeMesmaDataDoUsuario(obj.getSolicitantes(), obj);
        }
    }


    private void verificarConflitosDeAgendamento(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        if (!transacoesAprovadasOuConfirmadasConflitantes(dataHoraInicio, dataHoraFim).isEmpty()) {
            throw new AgendamentoException("Já existe agendamento aprovado pelo administrador ou confirmado pelo usuário para essa data");
        }
    }

    private void verificarMudancaProfessor(Agendamento agendamento) {
        StatusTransacaoItem statusTransacaoItem = agendamento.getStatusTransacaoItem ();
        if(!statusTransacaoItem.equals (EM_ANALISE) && !statusTransacaoItem.equals (APROVADO) && !statusTransacaoItem.equals (AGUARDANDO_CONFIRMACAO_PROFESSOR))
            throw new AtualizarAgendamentoException ("Mudança de professor somente se o status da transação for " +
                    "aprovada, confirmada ou em análise");

    }

}
