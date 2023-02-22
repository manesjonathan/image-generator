package com.jonathanmanes.imagegenerator.service;

import com.jonathanmanes.imagegenerator.model.User;
import com.jonathanmanes.imagegenerator.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Resource(name = "passwordEncoder")
    private final PasswordEncoder passwordEncoder;

    public Boolean createUser(String email, String password) {

        Optional<User> userByEmailAndPassword = findUserByEmail(email);
        if (userByEmailAndPassword.isEmpty()) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));

            userRepository.save(user);
            return true; //user is saved
        } else {
            return false; //user already exists
        }
    }


    private Optional<User> findUserByEmail(String email) {

        return userRepository.findUserByEmail(email);

    }

}
