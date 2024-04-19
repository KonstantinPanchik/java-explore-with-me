package ru.practicum.mainservice.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.user.dto.UserAdminDto;
import ru.practicum.mainservice.user.dto.UserMapper;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/${admin.path}/users")
@Slf4j
public class AdminUserController {

    UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers(@RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size,
                                              @RequestParam(required = false) List<Long> ids) {

        List<UserAdminDto> result = userService.getAll(from, size, ids)
                .stream()
                .map(UserMapper::toAdminDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid User user) {

        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toAdminDto(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {

        User user = userService.getById(userId);
        return ResponseEntity.ok(UserMapper.toAdminDto(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
