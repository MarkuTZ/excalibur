package com.example.controllers;

import com.example.models.Project;
import com.example.models.User;
import com.example.security.JwtUtils;
import com.example.services.ProjectService;
import com.example.services.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.controllers.LoginController.logedUser;


@RestController
@RequiredArgsConstructor
public class ProjectContoller {

    @Autowired
    private final ProjectService projectService;

    @Autowired
    private final JwtUtils jwtUtils;

    @PostMapping("/project")
    public ResponseEntity<?> registerProject(@RequestBody ObjectNode json) throws ParseException {
        String name = json.get("name").textValue();
        String description = json.get("description").textValue();
        String deadLine = json.get("deadline").textValue();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date deadline = df.parse(deadLine);

        Map<String, String> jsonResponse = new HashMap<>();
        if (projectService.existProject(name) == null) {

            Project project = projectService.saveProjectInDb(name, description ,deadline, logedUser);
            return ResponseEntity.ok(project);
        } else {
            jsonResponse.put("Error:", "Project already exist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }
    }
}
