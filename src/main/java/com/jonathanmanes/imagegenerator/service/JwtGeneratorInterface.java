package com.jonathanmanes.imagegenerator.service;

import com.jonathanmanes.imagegenerator.model.User;

import java.util.Map;

public interface JwtGeneratorInterface {
    Map<String, String> generateToken(User user);
}
