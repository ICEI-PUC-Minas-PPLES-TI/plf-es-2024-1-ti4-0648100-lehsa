package com.gerenciadorlehsa.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j

public class AuthenticationService {

    private static final String BEARER = "Bearer";

    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final SecretKey JWT_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    //private static final String JWT_KEY = "signinKey";

    private static final String AUTHORITIES = "authorities";

    private static final int EXPIRATION_TOKEN = 3600000;


    static public void addJWTToken(HttpServletResponse response, Authentication authentication) {

        // mapa de permissões
        Map<String, Object> claims = new HashMap<> ();

        claims.put (AUTHORITIES, authentication.getAuthorities ()
                .stream ()
                .map(GrantedAuthority :: getAuthority)
                .collect(Collectors.toList()));

        String jwtToken = Jwts.builder ()
                .setSubject (authentication.getName ())
                .setExpiration (new Date (System.currentTimeMillis () + EXPIRATION_TOKEN))
                .signWith(JWT_KEY)
                .addClaims (claims)
                .compact ();

        response.addHeader (HEADER_AUTHORIZATION, BEARER +" " + jwtToken);
        response.addHeader ("Access-Control-Expose-Headers", HEADER_AUTHORIZATION);

    }


    static public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader (HEADER_AUTHORIZATION);
        if(token != null) {
            Claims user = Jwts.parser ()
                    .setSigningKey (JWT_KEY)
                    .parseClaimsJws (token.replace (BEARER + " ", ""))
                    .getBody ();
            if(user != null){
                List<SimpleGrantedAuthority> permissoes = ((ArrayList<String>) user.get(AUTHORITIES))
                        .stream ()
                        .map (SimpleGrantedAuthority::new)
                        .toList ();


                return new UsernamePasswordAuthenticationToken (user, null, permissoes);
            }
             else{

                throw new RuntimeException ("Autenticação falhou");
            }

        }
        return null;
    }



}
