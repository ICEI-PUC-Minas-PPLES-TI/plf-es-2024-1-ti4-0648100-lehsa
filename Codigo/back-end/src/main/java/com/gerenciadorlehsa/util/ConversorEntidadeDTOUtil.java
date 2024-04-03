package com.gerenciadorlehsa.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import com.gerenciadorlehsa.controller.UsuarioControllerImpl;
import com.gerenciadorlehsa.dto.UsuarioDTO;
import com.gerenciadorlehsa.entity.User;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.CONVERSOR_ENTIDADE_DTO_UTIL;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
                .email(usuario.getEmail())
                .links(singletonList(linkTo(UsuarioControllerImpl.class).slash(usuario.getId()).withSelfRel()))
                .build();
    }

    /**
     * Converte uma entidade do tipo Endereco para EnderecoDTO
     *
     * @param endereco entidade do tipo Endereco
     * @return novo EnderecoDTO
     */

}