package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Professor;
import com.gerenciadorlehsa.exceptions.lancaveis.DeletarEntidadeException;
import com.gerenciadorlehsa.exceptions.lancaveis.EntidadeNaoEncontradaException;
import com.gerenciadorlehsa.exceptions.lancaveis.MensagemEmailException;
import com.gerenciadorlehsa.exceptions.lancaveis.ProfessorException;
import com.gerenciadorlehsa.repository.ProfessorRepository;
import com.gerenciadorlehsa.service.interfaces.OperacoesCRUDServiceImg;
import com.gerenciadorlehsa.service.interfaces.ProfessorService;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import com.gerenciadorlehsa.util.EstilizacaoEmailUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
public class ProfessorServiceImpl implements OperacoesCRUDServiceImg<Professor>, ProfessorService {

    private final ProfessorRepository professorRepository;

    private final MensagemEmailService mensagemEmailService;

    private final ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;

    private final String DIRETORIO_IMGS = "src/main/java/com/gerenciadorlehsa/util/imgs/prof";

    @Override
    public Professor encontrarPorId(@NotNull UUID id) {
        log.info(">>> encontrarPorId: encontrando professor por id");
        validadorAutorizacaoRequisicaoService.getUsuarioLogado ();

        return professorRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException (format("Professor não encontrado, id: %s", id)));
    }

    @Override
    public Professor criar(Professor obj, MultipartFile img) {
        log.info (">>> criar: criando professor");
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao ();
        obj.setId (null);
        obj.setConfirmaCadastro (true);
        obj.setDataHoraCriacao (LocalDateTime.now ());
        try {
            String nomeImagem = saveImageToStorage(img);
            obj.setCaminhoImg(nomeImagem);
            return this.professorRepository.save (obj);
        } catch (Exception e) {
            throw new ProfessorException(e.getMessage());
        }

        //todo: envio de e-mail por enquanto comentado
        /*enviarEmailParaProfessor(professor, ".../professor/confirmacao-cadastro?id=" + professor.getId ());*/

    }

    public String saveImageToStorage(MultipartFile imageFile) throws IOException {
        verificarTipoArquivo(imageFile);
        if (imageFile.getSize() > 250 * 1024) {
            throw new RuntimeException("Tamanho do arquivo para imagem de item excede 250 KB");
        }

        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        Path uploadPath = Path.of(DIRETORIO_IMGS);
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }


    @Override
    public Professor atualizar(Professor professor, MultipartFile img) {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao ();
        Professor professorAtualizado = encontrarPorId (professor.getId ());
        if (img.getContentType() == null) {
            professor.setCaminhoImg(professorAtualizado.getCaminhoImg());
        } else {
            verificarTipoArquivo(img);
            try {
                deleteImage(professorAtualizado.getCaminhoImg());
                professor.setCaminhoImg(saveImageToStorage(img));
            } catch (IOException e) {
                throw new ProfessorException(e.getMessage());
            }
        }

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
        Professor prof = encontrarPorId(id);
        try {
            deleteImage(prof.getCaminhoImg());
            this.professorRepository.deleteById(id);
            log.info(format(">>> deletar: professor deletado, id: %s", id));
        } catch (Exception e) {
            throw new DeletarEntidadeException (format("existem entidades relacionadas: %s", e));
        }
    }

    public String deleteImage(String imageName) throws IOException {
        Path imagePath = Path.of(DIRETORIO_IMGS, imageName);

        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
            return "Success";
        } else {
            return "Failed"; // Handle missing images
        }
    }

    @Override
    public List<Professor> listarTodos() {
        log.info(">>> listarTodos: listando todos os professores");
        validadorAutorizacaoRequisicaoService.getUsuarioLogado ();
        return professorRepository.findAll();
    }

    @Override
    public void enviarEmail(Professor professor, String linkConfirmacao) {
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

    private void verificarTipoArquivo (MultipartFile img) {
        if (img.getContentType() == null) {
            throw new RuntimeException("Arquivo para imagem de professor não recebido");
        }
        if (!(img.getContentType().equals("image/jpeg") || img.getContentType().equals("image/png"))) {
            throw new RuntimeException("Arquivo para imagem de item é um tipo não aceito");
        }
    }

    @Override
    public byte[] encontrarImagemPorId(@NotNull UUID id) {
        log.info(">>> encontrarImagemPorId: encontrando imagem por id");
        Professor profImg = encontrarPorId(id);
        try {
            return getImage(profImg.getCaminhoImg());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private byte[] getImage(String imageName) throws IOException {
        Path imagePath = Path.of(DIRETORIO_IMGS, imageName);

        if (Files.exists(imagePath)) {
            return Files.readAllBytes(imagePath);
        } else {
            throw new EntidadeNaoEncontradaException("Imagem não encontrada"); // Handle missing images
        }
    }

}
