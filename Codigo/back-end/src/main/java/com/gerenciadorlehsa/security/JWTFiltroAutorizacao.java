package com.gerenciadorlehsa.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.gerenciadorlehsa.components.security.JWTComp;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import java.io.IOException;
import static java.util.Objects.nonNull;
import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.HEADER_AUTORIZACAO;
import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.TIPO_TOKEN;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.JWT_FILTRO_AUTORIZACAO;
import static org.springframework.util.StringUtils.trimAllWhitespace;

@Slf4j(topic = JWT_FILTRO_AUTORIZACAO)
public class JWTFiltroAutorizacao extends BasicAuthenticationFilter {

    private final UsuarioDetailsService usuarioDetailsService;
    private final JWTComp jwtComp;

    public JWTFiltroAutorizacao(AuthenticationManager authenticationManager, JWTComp jwtComp, UsuarioDetailsService usuarioDetailsService) {
        super(authenticationManager);
        this.jwtComp = jwtComp;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    /**
     * Realiza filtro de autorização de usuário
     *
     * @param request     requisição
     * @param response    resposta
     * @param filterChain camada de segurança Filter Chain
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("===========================================================================");
        log.info(">>> doFilterInternal: realizando filtro de autorização");
        String headerAutorizacao = request.getHeader(HEADER_AUTORIZACAO);
        if (nonNull(headerAutorizacao) && headerAutorizacao.startsWith(TIPO_TOKEN)) {
            String token = trimAllWhitespace(headerAutorizacao).substring(TIPO_TOKEN.length());
            UsernamePasswordAuthenticationToken auth = getAutenticacao(token);
            if (nonNull(auth))
                SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Método responsável por receber um token e retornar uma autenticação
     *
     * @param token token do usuário
     * @return autenticação
     */
    private @Nullable UsernamePasswordAuthenticationToken getAutenticacao(String token) {
        log.info(">>> getAutenticacao: obtendo autenticação do usuário");
        if (this.jwtComp.tokenValido(token)) {
            String emailUsuario = this.jwtComp.getEmailUsuario(token);
            UserDetails usuario = this.usuarioDetailsService.loadUserByUsername(emailUsuario);
            return new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        }
        return null;
    }
}
