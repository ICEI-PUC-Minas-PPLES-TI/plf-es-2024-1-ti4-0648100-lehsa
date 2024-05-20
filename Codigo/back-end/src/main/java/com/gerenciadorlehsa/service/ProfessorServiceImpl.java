package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Professor;
import com.gerenciadorlehsa.exceptions.lancaveis.DeletarEntidadeException;
import com.gerenciadorlehsa.exceptions.lancaveis.EntidadeNaoEncontradaException;
import com.gerenciadorlehsa.exceptions.lancaveis.MensagemEmailException;
import com.gerenciadorlehsa.exceptions.lancaveis.ProfessorException;
import com.gerenciadorlehsa.repository.ProfessorRepository;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDService;
import com.gerenciadorlehsa.service.interfaces.ProfessorService;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import com.gerenciadorlehsa.util.EstilizacaoEmailUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.PROPRIEDADES_IGNORADAS;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.PROFESSOR_SERVICE;
import static java.lang.String.format;
import static org.springframework.beans.BeanUtils.copyProperties;

@Slf4j(topic = PROFESSOR_SERVICE)
@Service
@AllArgsConstructor
public class ProfessorServiceImpl implements OperacoesCRUDService<Professor>, ProfessorService {

    private final ProfessorRepository professorRepository;

    private final MensagemEmailService mensagemEmailService;

    private final ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;

    @Override
    public Professor encontrarPorId(@NotNull UUID id) {
        log.info(">>> encontrarPorId: encontrando professor por id");
        validadorAutorizacaoRequisicaoService.getUsuarioLogado ();

        return professorRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException (format("Professor não encontrado, id: %s", id)));
    }

    @Override
    public Professor criar (Professor obj) {
        log.info (">>> criar: criando professor");
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao ();
        obj.setId (null);
        obj.setConfirmaCadastro (false);
        obj.setDataHoraCriacao (LocalDateTime.now ());
        Professor professor = this.professorRepository.save (obj);

        //todo: envio de e-mail por enquanto comentado
        /*enviarEmailParaProfessor(professor, ".../professor/confirmacao-cadastro?id=" + professor.getId ());*/

        return professor;
    }


    @Override
    public Professor atualizar (Professor professor) {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao ();
        Professor professorAtualizado = encontrarPorId (professor.getId ());
        copyProperties(professor, professorAtualizado, PROPRIEDADES_IGNORADAS);

        // todo: Setando confirmação de cadastro como verdadeiro só para teste
        // professorAtualizado.setConfirmaCadastro (true);


        if(!Objects.equals (professor.getEmail (), professorAtualizado.getEmail ())) {
            professorAtualizado.setConfirmaCadastro (false);
            professorAtualizado = professorRepository.save (professorAtualizado);

            //todo: envio de e-mail por enquanto comentado
            /*enviarEmailParaProfessor (professorAtualizado, ".../professor/confirmacao-cadastro?id=" + professorAtualizado.getId ());*/
        } else
            professorAtualizado = professorRepository.save (professorAtualizado);


        return professorAtualizado;
    }

    @Override
    public void deletar(@NotNull UUID id) {
        log.info(">>> deletar: deletando professor");
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao ();
        encontrarPorId(id);
        try {
            this.professorRepository.deleteById(id);
            log.info(format(">>> deletar: professor deletado, id: %s", id));
        } catch (Exception e) {
            throw new DeletarEntidadeException (format("existem entidades relacionadas: %s", e));
        }
    }

    @Override
    public List<Professor> listarTodos() {
        log.info(">>> listarTodos: listando todos os professores");
        validadorAutorizacaoRequisicaoService.getUsuarioLogado ();
        return professorRepository.findAll();
    }

    @Override
    public void enviarEmailParaProfessor(Professor professor, String linkConfirmacao) {
        try {
            mensagemEmailService.enviarEmailConfirmacaoCadastro(professor.getEmail(), EstilizacaoEmailUtil.estilizaConfirmacao (linkConfirmacao));
        } catch (Exception e) {
            throw new MensagemEmailException ("Envio de e-mail falhou.");
        }
    }

    @Override
    public Professor confirmaEmail(UUID id, Boolean value) {
        Professor professor = encontrarPorId (id);
        log.info(">>> confirmaEmail: confirmando cadastro do professor");
        professor.setConfirmaCadastro (true);
        return professorRepository.save (professor);
    }

    @Override
    public Professor encontrarPorEmail(@NotNull String email) {
        log.info(">>> encontrarPorEmail: encontrando professor por email");
        return professorRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(format("professor não encontrado, email: %s",
                        email)));
    }


    public List<Agendamento> listarAgendamentos(UUID id) {
        Professor professor = encontrarPorId (id);
        if(professor.getAgendamentos () == null || professor.getAgendamentos ().isEmpty ())
            throw new ProfessorException ("O professor não possui agendamentos");
        return professor.getAgendamentos ();
    }

}
