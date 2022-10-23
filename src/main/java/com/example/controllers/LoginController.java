package com.example.controllers;

import com.example.models.User;
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
    UserService userService;

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody ObjectNode json) {

        String stringUsername = json.get("username").textValue();
        String stringPassword = json.get("password").textValue();
        Map<String, String> jsonResponse = new HashMap<>();
        User u;
        if ((u = userService.existUser(stringUsername)) != null) {
            if (!userService.checkPassword(u, stringPassword)) {
                jsonResponse.put("message", "Wrong credentials!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
            }
            String token = userService.generateToken(stringUsername);
            jsonResponse.put("token", token);
            return ResponseEntity.ok(jsonResponse);
        } else
        {
            jsonResponse.put("Error:", "User doesn't exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
        }

    }


}
