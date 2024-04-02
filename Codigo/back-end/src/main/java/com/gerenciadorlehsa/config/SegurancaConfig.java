package com.gerenciadorlehsa.config;

import com.gerenciadorlehsa.security.JWTFiltroAutenticacao;
import com.gerenciadorlehsa.security.JWTFiltroAutorizacao;
import com.gerenciadorlehsa.security.UsuarioDetailsService;
import com.gerenciadorlehsa.service.PasswordEncoderImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import com.gerenciadorlehsa.components.JWTComp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.*;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.SEGURANCA_CONFIG;
import static org.springframework.http.HttpMethod.POST;


@Slf4j(topic = SEGURANCA_CONFIG)
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SegurancaConfig {

    private final UsuarioDetailsService usuarioDetailsService;
    private final JWTComp jwtComp;

    /**
     * Cria camada de segurança Filter Chain
     *
     * @param httpSecurity configuração para papéis de usuário
     * @return configuração do httpSecurity
     * @throws Exception lança exceção caso haja problema na configuração
     */
    @Bean
    public SecurityFilterChain filterChain(@NotNull HttpSecurity httpSecurity) throws Exception {
        log.info(">>> filterChain: iniciando camada de segurança Filter Chain");
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(usuarioDetailsService).passwordEncoder(new PasswordEncoderImpl ());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager)
                .addFilter(new JWTFiltroAutenticacao (authenticationManager, this.jwtComp))
                .addFilter(new JWTFiltroAutorizacao (authenticationManager, this.jwtComp, this.usuarioDetailsService))
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(CAMINHOS_PUBLICOS).permitAll();
                    request.requestMatchers(POST, CAMINHOS_PUBLICOS_POST).permitAll();
                    request.anyRequest().authenticated();
                })
                .build();
    }

    /**
     * Configuração de cors
     *
     * @return configuração de cors
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        log.info(">>> corsConfigurationSource: iniciando configuração de Cors");
        final CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        configuration.setAllowedMethods(METODOS_PERMITIDOS_CORS);
        configuration.addAllowedOrigin("http://localhost:3000/");
        source.registerCorsConfiguration(CONFIGURACAO_CORS, configuration);


        return source;
    }


}
