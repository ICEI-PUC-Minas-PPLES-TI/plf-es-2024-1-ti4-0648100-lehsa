package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional //Fecha conexão com o BD depois de finalizada a execução do método
public class AuthenticatedUserService implements UserDetailsService {

    @Autowired
    private IUserRepository iUserRepository;

    public UserDetails loadUserByUsername(String username) {
        User user = iUserRepository.findByUsername (username).
                orElseThrow (() -> new UsernameNotFoundException ("Usuário " + username +
                        " não foi encontrado"));

        List<SimpleGrantedAuthority> roles = user.getRoles ()
                .stream ()
                .map (role -> new SimpleGrantedAuthority (role.getNome ().toString ()))
                .collect(Collectors.toList ());

        return new org.springframework.security.core.userdetails.User (user.getUsername (),
                user.getPassword (), roles);
    }
}
