package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAllUsers() {
        return this.iUserRepository.findAll ();
    }

    public User create(User user) {

        //Senha criptografada
        user.setPassword (passwordEncoder.encode (user.getPassword ()));

        return this.iUserRepository.save (user);
    }

    public User update(User user) {
        user.setPassword (passwordEncoder.encode (user.getPassword ()));
        return this.iUserRepository.save (user); // Se tiver cadastrado ele só atualiza.
    }

    public void delete(User user) {
        this.iUserRepository.deleteById (user.getId ());
    }


}
