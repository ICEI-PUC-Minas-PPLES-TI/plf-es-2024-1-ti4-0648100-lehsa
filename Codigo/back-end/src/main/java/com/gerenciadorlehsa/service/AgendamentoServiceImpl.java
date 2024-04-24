package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.exceptions.lancaveis.EntidadeNaoEncontradaException;
import com.gerenciadorlehsa.exceptions.lancaveis.UsuarioNaoAutorizadoException;
import com.gerenciadorlehsa.repository.AgendamentoRepository;
import com.gerenciadorlehsa.security.UsuarioDetails;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.UUID;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.AGENDAMENTO_SERVICE;
import static java.lang.String.format;

@Slf4j(topic = AGENDAMENTO_SERVICE)
@Service
@AllArgsConstructor
public class AgendamentoServiceImpl implements OperacoesCRUDService<Agendamento> {

    //private

    private final AgendamentoRepository agendamentoRepository;

    private final ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;

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



    /*public void verificarRelacaoUsuarioAgendamento(Agendamento agendamento) {
        UsuarioDetails usuarioDetails = validadorAutorizacaoService.
    }*/



    @Override
    public Agendamento criar (Agendamento obj) {
        return null;
    }

    @Override
    public Agendamento atualizar (Agendamento obj) {
        return null;
    }

    @Override
    public void deletar (UUID id) {

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


}
