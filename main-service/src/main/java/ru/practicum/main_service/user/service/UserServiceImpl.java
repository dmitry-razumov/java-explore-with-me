package ru.practicum.main_service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;
import ru.practicum.main_service.utils.PageRequestCustom;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User registerUser(User user) {
        User newUser = userRepository.save(user);
        log.info("создан пользователь {}", newUser);
        return newUser;
    }

    @Override
    public List<User> getUsers(List<Long> ids, int from, int size) {
        List<User> userList;
        if (ids != null && !ids.isEmpty()) {
            userList = userRepository.findAllById(ids);
        } else {
            Pageable page = PageRequestCustom.get(from, size);
            userList = userRepository.findAllByOrderById(page);
        }
        log.info("получены пользователи {}", userList);
        return userList;
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        userRepository.deleteById(userId);
        log.info("удален пользователь c id={}", userId);
    }
}
