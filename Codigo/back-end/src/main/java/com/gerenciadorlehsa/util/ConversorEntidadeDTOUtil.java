package com.gerenciadorlehsa.util;

import com.gerenciadorlehsa.dto.*;
import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import com.gerenciadorlehsa.entity.User;
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


      /*  List<ItemDTORes> itensDTO = agendamento.getItens() != null ?
                agendamento.getItens().stream()
                        .map(ConversorEntidadeDTOUtil::converterItemParaDTORes)
                        .collect(Collectors.toList()) :
                null;*/


        UsuarioShortDTORes tecnicoDTO = agendamento.getTecnico() != null ?
                converterUsuarioParaShortDTORes(agendamento.getTecnico()) :
                null;

        return AgendamentoDTORes.builder()
                .id(agendamento.getId())
                .dataHoraInicio(converterDataHora(agendamento.getDataHoraInicio()))
                .dataHoraFim(converterDataHora(agendamento.getDataHoraFim()))
                .observacaoSolicitacao(agendamento.getObservacaoSolicitacao())
                .statusTransacaoItem(agendamento.getStatusTransacaoItem())
                .solicitantes(solicitantesDTO)
                //.itens(itensDTO)
                .tecnico(tecnicoDTO)
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

    public static ItemDTORes converterItemParaDTORes (@NotNull Item item) {
        return ItemDTORes.builder ()
                .id (item.getId ())
                .nome (item.getNome ())
                .tipoItem (item.getTipoItem () != null ? String.valueOf (item.getTipoItem ()) : null)
                .build ();
    }

}
