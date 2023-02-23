package com.jonathanmanes.imagegenerator.controller;

import com.jonathanmanes.imagegenerator.model.User;
import com.jonathanmanes.imagegenerator.service.AIService;
import com.jonathanmanes.imagegenerator.service.JwtGeneratorImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageGeneratorController {

    private final AIService aiService;
    private final JwtGeneratorImpl jwtGenerator;

    @PostMapping("/generate")
    public ResponseEntity<?> generateImage(@RequestBody User user, @RequestParam String prompt, @RequestHeader("Authorization") String authHeader) {

        boolean validateJwtToken = jwtGenerator.validateToken(authHeader);
        if (validateJwtToken) {
            return new ResponseEntity<>(aiService.generatePicture(prompt), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }
}
