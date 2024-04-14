package ru.practicum.mainservice.user.dto;

import ru.practicum.mainservice.user.model.User;

public class UserMapper {

    public static UserShortDto toShortDto(User user) {

        return new UserShortDto(user.getId(), user.getName());
    }

}
