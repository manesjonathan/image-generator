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
public class UserServiceImpl implements UserServiceInterface {

    private final UserRepository userRepository;

    @Resource(name = "passwordEncoder")
    private final PasswordEncoder passwordEncoder;

    @Override
    public Boolean createUser(User user) {
        User userByEmailAndPassword = findUserByEmailAndPassword(user);
        if (userByEmailAndPassword == null) {
            User userToSave = new User();
            userToSave.setEmail(user.getEmail());
            userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(userToSave);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User findUserByEmailAndPassword(User user) {
        Optional<User> userByEmail = userRepository.findUserByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), userByEmail.get().getPassword())) {
                return userByEmail.get();
            }
        }
        return null;
    }
}
