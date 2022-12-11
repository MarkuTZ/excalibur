package com.example.controllers;

import com.example.models.User;
import com.example.security.JwtUtils;
import com.example.services.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtUtils jwtUtils;

    static User logedUser ;
    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody ObjectNode json) {
        logedUser=new User();
        String stringUsername = json.get("username").textValue();
        String stringPassword = json.get("password").textValue();
        Map<String, String> jsonResponse = new HashMap<>();
        User u;
        if ((u = userService.existUser(stringUsername)) != null) {
            if (!userService.checkPassword(u, stringPassword)) {
                jsonResponse.put("message", "Wrong credentials!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
            }
            String token = jwtUtils.generateToken(stringUsername);
            jsonResponse.put("token", token);
            logedUser.setId(userService.getUser(jwtUtils.getSubject(token)).getId());
            logedUser.setUsername(stringUsername);
            logedUser.setPassword(stringPassword);
            return ResponseEntity.ok(jsonResponse);
        } else {
            jsonResponse.put("Error:", "User doesn't exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody ObjectNode json) {
        String stringUsername = json.get("username").textValue();
        String stringPassword = json.get("password").textValue();
        Map<String, String> jsonResponse = new HashMap<>();
        if (userService.existUser(stringUsername) == null) {
            User u  = userService.saveUserInDb(stringUsername, stringPassword);
            return ResponseEntity.ok(u);
        } else {
            jsonResponse.put("Error:", "User already exist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }
    }
}
