package com.byxm.services;

import com.byxm.model.User;

import java.util.List;

public interface UserService {
    User getUser(String username);

    String register(String username, String password);

    int addAmount(String username, Double amount);

    int modifyUser(User user);

    List<User> getAllUser();

    List<User> getUserPage(Integer page, Integer size,String username,String power);

    User getUserById(Integer id);

    int getUserCount();

    int removeUserByid(Integer id);
}
