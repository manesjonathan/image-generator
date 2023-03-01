package com.jonathanmanes.imagegenerator.service;

import com.jonathanmanes.imagegenerator.model.User;
import com.jonathanmanes.imagegenerator.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Resource(name = "passwordEncoder")
    private final PasswordEncoder passwordEncoder;

    public Boolean createUser(User user) {
        User userByEmailAndPassword = findUserByEmailAndPassword(user);
        if (userByEmailAndPassword == null) {
            User userToSave = new User();
            userToSave.setEmail(user.getEmail());
            userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
            userToSave.setAllowed(true);
            userToSave.setQuota(1);
            userRepository.save(userToSave);
            return true;
        } else {
            return false;
        }
    }

    public User findUserByEmailAndPassword(User user) {
        Optional<User> userByEmail = userRepository.findUserByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), userByEmail.get().getPassword())) {
                return userByEmail.get();
            }
        }
        return null;
    }

    public User findUserByEmail(String email) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        return userByEmail.orElse(null);
    }

    public boolean isUserAllowed(String email) {
        boolean isAllowed = findUserByEmail(email).isAllowed;
        if (!isAllowed) {
            return false;
        }
        User user = findUserByEmail(email);
        return updateUserQuota(user);
    }

    public boolean updateUserQuota(User user) {
        if (user.getQuota() > 0) {
            user.setQuota(user.getQuota() - 1);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
