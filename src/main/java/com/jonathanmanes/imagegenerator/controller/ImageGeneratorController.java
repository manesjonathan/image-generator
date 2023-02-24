package com.jonathanmanes.imagegenerator.controller;

import com.jonathanmanes.imagegenerator.model.User;
import com.jonathanmanes.imagegenerator.service.AIService;
import com.jonathanmanes.imagegenerator.service.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageGeneratorController {

    private final AIService aiService;
    private final JwtUtils jwtUtils;

    @CrossOrigin
    @PostMapping("/generate")
    public ResponseEntity<?> generateImage(@RequestBody String prompt, @RequestHeader("Authorization") String authHeader) {

        boolean validateJwtToken = jwtUtils.validateToken(authHeader);
        if (validateJwtToken) {
            return new ResponseEntity<>(aiService.generatePicture(prompt), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }
}
