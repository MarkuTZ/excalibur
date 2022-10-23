package com.example.controllers;

import com.example.models.User;
import com.example.repositories.UserRepository;
import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping ("/token")
    public String getToken(@RequestParam("username") String username, @RequestParam("password") String password)
    {
        String token;
        token= userService.generateToken(username,password);
        if (token.isBlank())
        {
            return "no token found";
        }
        else return token;
    }

}
