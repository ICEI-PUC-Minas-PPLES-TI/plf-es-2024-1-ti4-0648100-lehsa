package com.gerenciadorlehsa.config;

import com.gerenciadorlehsa.security.JWTFiltroAutenticacao;
import com.gerenciadorlehsa.security.JWTFiltroAutorizacao;
import com.gerenciadorlehsa.security.UserDetailsServiceImpl;
import com.gerenciadorlehsa.service.PasswordEncoderServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import com.gerenciadorlehsa.components.security.JWTComp;
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
import java.util.Arrays;
import static com.gerenciadorlehsa.util.ConstantesRequisicaoUtil.*;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.SEGURANCA_CONFIG;
import static org.springframework.http.HttpMethod.POST;


@Slf4j(topic = SEGURANCA_CONFIG)
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SegurancaConfig {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JWTComp jwtComp;

    @Bean
    public SecurityFilterChain filterChain(@NotNull HttpSecurity httpSecurity) throws Exception {
        log.info(">>> filterChain: iniciando camada de segurança Filter Chain");
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsServiceImpl).passwordEncoder(new PasswordEncoderServiceImpl ());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Alterado para habilitar CORS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager)
                .addFilter(new JWTFiltroAutenticacao(authenticationManager, this.jwtComp))
                .addFilter(new JWTFiltroAutorizacao(authenticationManager, this.jwtComp, this.userDetailsServiceImpl))
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(CAMINHOS_PUBLICOS).permitAll();
                    request.requestMatchers(POST, CAMINHOS_PUBLICOS_POST).permitAll();
                    request.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        log.info(">>> corsConfigurationSource: iniciando configuração de Cors");
        final CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowCredentials(true); // Permitir credenciais
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a configuração para todas as rotas

        return source;
    }
}
