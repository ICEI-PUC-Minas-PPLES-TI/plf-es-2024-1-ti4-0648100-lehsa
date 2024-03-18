package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    public User create(User user) {
        return this.iUsuarioRepository.save (user);
    }
}
