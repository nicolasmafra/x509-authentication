package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Usuario getUsuario() {
        return userService.getUsuarioAutenticado();
    }
}
