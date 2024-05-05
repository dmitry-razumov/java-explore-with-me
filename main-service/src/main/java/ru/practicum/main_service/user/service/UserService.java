package ru.practicum.main_service.user.service;

import ru.practicum.main_service.user.model.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);

    List<User> getUsers(List<Long> ids, int from, int size);

    void delete(Long userId);
}
