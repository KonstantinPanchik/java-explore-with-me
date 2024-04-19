package ru.practicum.mainservice.user.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.user.dto.UserMapper;
import ru.practicum.mainservice.user.dto.UserSubscribeDto;
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

    @Override
    public UserSubscribeDto subscribe(Long userId, Long bloggerId) {

        User user = getById(userId);
        User blogger = getById(bloggerId);

        if (user.equals(blogger)) {
            throw new ConflictException("You can't subscribe your account");
        }

        user.getSubscriptions().add(blogger);

        userRepository.save(user);

        return UserMapper.toSubscribeDto(user);
    }

    @Override
    public UserSubscribeDto unSubscribe(Long userId, Long bloggerId) {

        User user = getById(userId);
        User blogger = getById(bloggerId);

        boolean isSubscribe = user.getSubscriptions().contains(blogger);

        if (isSubscribe) {
            user.getSubscriptions().remove(blogger);
        } else {
            throw new NotFoundException("You are not subscribe on this user");
        }

        userRepository.save(user);

        return UserMapper.toSubscribeDto(user);
    }

}
