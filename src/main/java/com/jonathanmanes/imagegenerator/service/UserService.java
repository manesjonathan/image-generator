package com.jonathanmanes.imagegenerator.service;

import com.jonathanmanes.imagegenerator.model.User;
import com.jonathanmanes.imagegenerator.repository.RoleRepository;
import com.jonathanmanes.imagegenerator.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Resource(name = "passwordEncoder")
    private final PasswordEncoder passwordEncoder;

    @Value("${custom.admin.email}")
    private String adminEmail;

    public Boolean createUser(User user) {
        User userByEmailAndPassword = findUserByEmailAndPassword(user);
        if (userByEmailAndPassword == null) {
            User userToSave = new User();
            userToSave.setEmail(user.getEmail());
            userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
            userToSave.setAllowed(true);
            userToSave.setQuota(1);
            if (userToSave.getEmail().equals(adminEmail)) {
                userToSave.setRoles(roleRepository.findAll());
            } else {
                userToSave.setRoles(Collections.singletonList(roleRepository.findRoleByName("USER")));
            }
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
        boolean isAllowed = findUserByEmail(email).isAllowed();
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

    public void refillUser(String email) {
        User user = findUserByEmail(email);
        user.setQuota(5);
        userRepository.save(user);
    }
}
