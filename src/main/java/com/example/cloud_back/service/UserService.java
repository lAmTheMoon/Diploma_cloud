package com.example.cloud_back.service;

import com.example.cloud_back.model.Authority;
import com.example.cloud_back.model.User;

public interface UserService {

    User saveUser(User user);

    Authority saveAuthority(Authority authority);

    void addAuthorityToUser(User user, String authorityName);

    User getUser(String username);
}
