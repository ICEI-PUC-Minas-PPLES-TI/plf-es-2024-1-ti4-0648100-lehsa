package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario") //raíz da API
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> create (@RequestBody User user) {

        return new ResponseEntity<> (userService.create (user), HttpStatus.OK);
    }

}
