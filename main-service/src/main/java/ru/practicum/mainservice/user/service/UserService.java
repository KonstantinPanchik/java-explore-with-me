package ru.practicum.mainservice.user.service;

import ru.practicum.mainservice.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getAll(int from, int size, List<Long> ids);

    User getById(Long userId);

    void deleteUser(Long userId);

    User addUser(User user);
}
