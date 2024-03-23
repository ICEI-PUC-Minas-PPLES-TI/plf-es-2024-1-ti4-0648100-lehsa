package com.gerenciadorlehsa.config;

import com.gerenciadorlehsa.filter.AuthenticationFilter;
import com.gerenciadorlehsa.filter.LoginFilter;
import com.gerenciadorlehsa.permissions.PermissionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{


    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    private static final String[] PUBLIC_MATCHERS_POST = {
            "/usuario", "/login"
    };

    public static final String[] CAMINHOS_PUBLICOS = {"/"};



    @Bean //Criptografa o password do usuário
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder ();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager ();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.csrf (AbstractHttpConfigurer :: disable)
                .authorizeHttpRequests (auth ->
                        auth.requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                                .requestMatchers (CAMINHOS_PUBLICOS).permitAll ()
                       .requestMatchers (HttpMethod.GET, "/teste-api").permitAll ()
                        .requestMatchers (HttpMethod.GET, "/teste-api-bem-vindo").hasAuthority (PermissionEnum.ADMIN.toString ())
                        .requestMatchers (HttpMethod.GET, "/usuario").hasAuthority (PermissionEnum.ADMIN.toString ())
                        .anyRequest ()
                        .authenticated ());


        http.addFilterBefore (new LoginFilter ("/login", authenticationConfiguration.getAuthenticationManager ()),
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore (new AuthenticationFilter (), UsernamePasswordAuthenticationFilter.class);


        return http.build ();
    }


}

// permitAll() - a rota é disponível para o público
// hasAuthority - a rota é acessada somente para quem tem alguma autoridade especificada no parâmetro do método
// addFilterBefore - adicionar filtros
// /login é o caminho para entrar no sistema
