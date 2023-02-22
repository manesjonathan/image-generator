package com.jonathanmanes.imagegenerator.controller;

import com.jonathanmanes.imagegenerator.model.User;
import com.jonathanmanes.imagegenerator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        if (userService.createUser(user.getEmail(), user.getPassword())) {
            return new ResponseEntity<>("true", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("false", HttpStatus.FORBIDDEN);
        }
    }
}
