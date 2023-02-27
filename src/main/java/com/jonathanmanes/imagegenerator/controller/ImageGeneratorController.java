package com.jonathanmanes.imagegenerator.controller;

import com.jonathanmanes.imagegenerator.service.AIService;
import com.jonathanmanes.imagegenerator.service.JwtUtils;
import com.jonathanmanes.imagegenerator.service.StorageService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class ImageGeneratorController {

    private final AIService aiService;
    private final StorageService storageService;

    @Resource(name = "jwtUtils")
    private JwtUtils jwtUtils;

    @PostMapping("/generate")
    public ResponseEntity<?> generateImage(@RequestBody String prompt, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        boolean validateJwtToken = jwtUtils.validateToken(token);
        if (validateJwtToken) {
            Authentication auth = jwtUtils.getAuthentication(authHeader);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new ResponseEntity<>(aiService.generatePicture(prompt), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/get-history")
    public ResponseEntity<?> getHistory(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        boolean validateJwtToken = jwtUtils.validateToken(token);
        if (validateJwtToken) {
            Authentication auth = jwtUtils.getAuthentication(authHeader);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new ResponseEntity<>(storageService.listObjects(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }
}
