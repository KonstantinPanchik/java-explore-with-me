package ru.practicum.mainservice.user.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;
import ru.practicum.mainservice.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll(int from, int size, List<Long> ids) {
        if (ids == null || ids.isEmpty() || ids.get(0) == 0) {
            return userRepository.findAll(PageRequest.of(from / size, size)).stream()
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllById(ids);
        }
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        getById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }
}
