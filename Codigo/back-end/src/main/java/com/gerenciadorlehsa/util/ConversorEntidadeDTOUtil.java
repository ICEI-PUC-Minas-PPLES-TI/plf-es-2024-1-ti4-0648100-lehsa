package com.gerenciadorlehsa.util;

import com.gerenciadorlehsa.dto.*;
import com.gerenciadorlehsa.entity.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import static com.gerenciadorlehsa.util.DataHoraUtil.converterDataHora;
import static java.lang.String.format;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.CONVERSOR_ENTIDADE_DTO_UTIL;

@UtilityClass
@Slf4j(topic = CONVERSOR_ENTIDADE_DTO_UTIL)
public class ConversorEntidadeDTOUtil {

    /**
     * Converte uma entidade do tipo Usuario para UsuarioDTO
     *
     * @param usuario entidade do tipo Usuario
     * @return novo UsuarioDTO
     */
    public static UsuarioDTO converterParaDTO(@NotNull User usuario) {
        log.info(format(">>> converterParaDTO: convertendo Usuario (id: %s) para DTO", usuario.getId()));
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .perfilUsuario(usuario.getPerfilUsuario())
                .nome(usuario.getNome ())
                .cpf(usuario.getCpf())
                .telefone (usuario.getTelefone ())
                .tipoCurso (usuario.getTipoCurso () != null ? usuario.getTipoCurso ().toString () : null)
                .statusCurso (usuario.getStatusCurso () != null ? usuario.getStatusCurso ().toString () : null)
                .nota (usuario.getNota ())
                .curso (usuario.getCurso ())
                .email(usuario.getEmail())
                .build();
    }


    public static ProfessorDTO converterParaDTO(@NotNull Professor professor) {
        log.info(format(">>> converterParaDTO: convertendo Professor (id: %s) para DTO", professor.getId()));
        return ProfessorDTO.builder()
                .campus (professor.getCampus ())
                .areaAtuacao (professor.getAreaAtuacao ())
                .confirmaCadastro (professor.getConfirmaCadastro () ? "Cadastro confirmado" : "Cadastro sem " +
                        "confirmação")
                .lotacao (professor.getLotacao ())
                .matricula (professor.getMatricula ())
                .nome (professor.getNome ())
                .id (professor.getId ())
                .laboratorio (professor.getLaboratorio ())
                .email (professor.getEmail ())
                .dataHoraCadastro (converterDataHora (professor.getDataHoraCriacao ()))
                .build();
    }


    /**
     * Converter uma entidade Item para ItemDTO
     * @param item
     * @return
     */

    public static ItemDTO converterParaDTO(@NotNull Item item) {
        log.info(format(">>> converterParaDTO: convertendo Item (id: %s) para DTO", item.getId()));
        return ItemDTO.builder()
                .id(item.getId())
                .tipoItem(item.getTipoItem().name())
                .nome(item.getNome())
                .valorUnitario(item.getValorUnitario())
                .emprestavel(item.getEmprestavel())
                .quantidade(item.getQuantidade())
                .build();
    }

    public static AgendamentoDTORes converterParaDtoRes(@NotNull Agendamento agendamento) {
        log.info(format(">>> converterParaDTORes: convertendo Agendamento (id: %s) para DTORes", agendamento.getId()));


        List<UsuarioShortDTORes> solicitantesDTO = agendamento.getSolicitantes() != null ?
                agendamento.getSolicitantes().stream()
                        .map(ConversorEntidadeDTOUtil::converterUsuarioParaShortDTORes)
                        .collect(Collectors.toList()) :
                null;

        List<ItemDTORes> itensDTO = agendamento.getItensQuantidade() != null ?
                agendamento.getItensQuantidade().entrySet().stream()
                        .map(entry -> {
                            Item item = entry.getKey();
                            Integer quantidade = entry.getValue();
                            ItemDTORes itemDTO = ConversorEntidadeDTOUtil.converterItemParaDTORes(item, quantidade);
                            return itemDTO;
                        })
                        .collect(Collectors.toList()) :
                null;


        UsuarioShortDTORes tecnicoDTO = agendamento.getTecnico() != null ?
                converterUsuarioParaShortDTORes(agendamento.getTecnico()) :
                null;

        return AgendamentoDTORes.builder()
                .id(agendamento.getId())
                .dataHoraInicio(converterDataHora(agendamento.getDataHoraInicio()))
                .dataHoraFim(converterDataHora(agendamento.getDataHoraFim()))
                .observacaoSolicitacao(agendamento.getObservacaoSolicitacao())
                .statusTransacao(agendamento.getStatusTransacao ())
                .solicitantes(solicitantesDTO)
                .itens(itensDTO)
                .tecnico(tecnicoDTO)
                .build();
    }

    public static EmprestimoDTORes converterParaDtoRes(@NotNull Emprestimo emprestimo) {
        log.info(format(">>> converterParaDTORes: convertendo Emprestimo (id: %s) para DTORes", emprestimo.getId()));

        UsuarioShortDTORes solicitanteDTO = emprestimo.getSolicitante() != null ?
                converterUsuarioParaShortDTORes(emprestimo.getSolicitante()) :
                null;

        List<ItemDTORes> itensDTO = emprestimo.getItensQuantidade() != null ?
                emprestimo.getItensQuantidade().entrySet().stream()
                        .map(entry -> {
                            Item item = entry.getKey();
                            Integer quantidade = entry.getValue();
                            return ConversorEntidadeDTOUtil.converterItemParaDTORes(item, quantidade);
                        })
                        .collect(Collectors.toList()) :
                null;

        EnderecoDTORes enderecoDTORes = emprestimo.getLocalUso() != null ?
                converterEnderecoParaEnderecoDTORes(emprestimo.getLocalUso()) :
                null;

        return EmprestimoDTORes.builder()
                .id(emprestimo.getId())
                .dataHoraInicio(converterDataHora(emprestimo.getDataHoraInicio()))
                .dataHoraFim(converterDataHora(emprestimo.getDataHoraFim()))
                .observacaoSolicitacao(emprestimo.getObservacaoSolicitacao())
                .statusTransacao(emprestimo.getStatusTransacao ())
                .solicitante(solicitanteDTO)
                .itens(itensDTO)
                .endereco(enderecoDTORes)
                .build();
    }

    private EnderecoDTORes converterEnderecoParaEnderecoDTORes (@NotNull Endereco endereco) {
        return EnderecoDTORes.builder()
                .id (endereco.getId())
                .cep (endereco.getCep())
                .uf (endereco.getUf())
                .cidade (endereco.getCidade())
                .bairro (endereco.getBairro())
                .rua (endereco.getRua())
                .numero (endereco.getNumero())
                .complemento (endereco.getComplemento())
                .build();
    }
    public static UsuarioShortDTORes converterUsuarioParaShortDTORes (@NotNull User usuario){
        return UsuarioShortDTORes.builder ()
                .nome (usuario.getNome ())
                .email (usuario.getEmail())
                .telefone (usuario.getTelefone())
                .curso (usuario.getCurso ())
                .build ();
    }

    public static ItemDTORes converterItemParaDTORes (@NotNull Item item, Integer qtd) {
        return ItemDTORes.builder ()
                .id (item.getId ())
                .nome (item.getNome ())
                .tipoItem (item.getTipoItem () != null ? String.valueOf (item.getTipoItem ()) : null)
                .quantidade (qtd)
                .build ();
    }

}
