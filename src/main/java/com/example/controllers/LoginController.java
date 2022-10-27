package com.example.controllers;

import com.example.services.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (userService.existUser(stringUsername, stringPassword)) {
            String token = userService.generateToken(stringUsername);
            jsonResponse.put("token", token);
        } else jsonResponse.put("Error:", "User doesn't exist");
        return ResponseEntity.ok(jsonResponse);
    }


}
