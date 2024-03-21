package com.gerenciadorlehsa.controller;

import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario") //raíz do API
public class UserController {

    //Deixar de usar `@Autowired` para evitar o alto acoplamento da classe aos detalhes de implementação da injeção de
    // dependência
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> create (@Valid @RequestBody User user) {

        return new ResponseEntity<> (userService.create (user), HttpStatus.CREATED);
        // Caso consiga salvar o usuário, o status retornado é de OKAY
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        return new ResponseEntity<> (userService.update (user), HttpStatus.OK);
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
