package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.service.UserService;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuario") //raíz do API
public class UserController {


    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody User usuario) {

        User usuarioSalvo  = userService.create (usuario);
        return new ResponseEntity<>("Novo usuário criado "+ usuarioSalvo.getUsername(), HttpStatus.CREATED);
    }



    @PutMapping
    public ResponseEntity<String> update(@Valid @RequestBody User user) {

        User userUpdate = userService.update (user);

        return new ResponseEntity<> ("Usuário atualizado: " + userUpdate.getUsername (), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> listAllUsers() {
        return new ResponseEntity<> (userService.listAllUsers (), HttpStatus.OK);
    }


    @DeleteMapping
    public void delete(@RequestBody User user) {
        userService.delete (user);
    }



}
