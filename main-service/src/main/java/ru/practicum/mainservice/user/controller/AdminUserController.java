package ru.practicum.mainservice.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

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

        return ResponseEntity.ok(userService.getAll(from, size, ids));
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid User user) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {

        return ResponseEntity.ok(userService.getById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
