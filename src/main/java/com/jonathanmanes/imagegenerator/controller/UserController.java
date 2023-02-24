package com.jonathanmanes.imagegenerator.controller;

import com.jonathanmanes.imagegenerator.model.User;
import com.jonathanmanes.imagegenerator.service.JwtUtils;
import com.jonathanmanes.imagegenerator.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(User user) {
        if (userService.createUser(user)) {
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        User userData = userService.findUserByEmailAndPassword(user);

        if (userData == null) {
            return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(jwtUtils.generateToken(user), HttpStatus.OK);
    }
}
