package com.gerenciadorlehsa.util;

import com.gerenciadorlehsa.dto.AgendamentoDTO;
import com.gerenciadorlehsa.dto.ItemDTO;
import com.gerenciadorlehsa.entity.Agendamento;
import com.gerenciadorlehsa.entity.Item;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.entity.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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


    public static AgendamentoDTO converterParaDto(@NotNull Agendamento agendamento) {
        log.info(format(">>> converterParaDTO: convertendo Item (id: %s) para DTO", agendamento.getId()));


        List<UsuarioDTO> solicitantesDTO = agendamento.getSolicitantes() != null ?
                agendamento.getSolicitantes().stream()
                        .map(ConversorEntidadeDTOUtil::converterParaDTO)
                        .collect(Collectors.toList()) :
                null;


        List<ItemDTO> itensDTO = agendamento.getItens() != null ?
                agendamento.getItens().stream()
                        .map(ConversorEntidadeDTOUtil::converterParaDTO)
                        .collect(Collectors.toList()) :
                null;


        UsuarioDTO tecnicoDTO = agendamento.getTecnico() != null ?
                converterParaDTO(agendamento.getTecnico()) :
                null;

        return AgendamentoDTO.builder()
                .id(agendamento.getId())
                .dataHoraInicio(converterDataHora(agendamento.getDataHoraInicio()))
                .dataHoraFim(converterDataHora(agendamento.getDataHoraFim()))
                .observacaoSolicitacao(agendamento.getObservacaoSolicitacao())
                .statusTransacaoItem(agendamento.getStatusTransacaoItem())
                .solicitantes(solicitantesDTO)
                .itens(itensDTO)
                .tecnico(tecnicoDTO)
                .build();
    }



    private byte[] getImage(String imageDirectory, String imageName) throws IOException {
        Path imagePath = Path.of(imageDirectory, imageName);

        if (Files.exists(imagePath)) {
            byte[] imageBytes = Files.readAllBytes(imagePath);
            return imageBytes;
        } else {
            return null; // Handle missing images
        }
    }


/*
    public static Item converterParaItem(@NotNull ItemDTO itemDTO){
        log.info(">>> converterParaItem: convertendo DTO para Item");

        Item novoItem = new Item();
        novoItem.setId(null);
        novoItem.setTipoItem(TipoItem.valueOf(itemDTO.tipoItem()));
        novoItem.setNome(itemDTO.nome());
        novoItem.setValorUnitario(itemDTO.valorUnitario());
        novoItem.setEmprestavel(itemDTO.emprestavel());
        novoItem.setQuantidade(itemDTO.quantidade());

        return novoItem;
    }
    public static ItemDTOResponse converterParaDTOResponse(@NotNull Item item) {
        log.info(format(">>> converterParaDTO: convertendo Item (id: %s) para DTO", item.getId()));
        try {
            return ItemDTOResponse.builder()
                    .id(item.getId())
                    .tipoItem(item.getTipoItem().name())
                    .nome(item.getNome())
                    .valorUnitario(item.getValorUnitario())
                    .emprestavel(item.getEmprestavel())
                    .quantidade(item.getQuantidade())
                    .bytesImagem(getImage("Codigo/back-end/src/main/java/com/gerenciadorlehsa/util/imgs",
                            item.getNomeImg()))
                    .build();
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
*/


}
