package com.example.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody ObjectNode json) {

        System.out.println(json.get("username"));
        System.out.println(json.get("password"));


        Map<String, String> jsonResponse = new HashMap<>();
        //Put generated TOKEN HERE
        jsonResponse.put("token", "GENERATEDTOKEN");
        return ResponseEntity.ok(jsonResponse);
    }

}
