package com.jonathanmanes.imagegenerator.service;

import com.jonathanmanes.imagegenerator.model.User;

public interface UserServiceInterface {

    Boolean createUser(User user);

    User findUserByEmailAndPassword(User user);

}

