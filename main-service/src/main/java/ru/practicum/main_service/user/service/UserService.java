package ru.practicum.main_service.user.service;

import ru.practicum.main_service.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto registerUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    void delete(Long userId);
}
