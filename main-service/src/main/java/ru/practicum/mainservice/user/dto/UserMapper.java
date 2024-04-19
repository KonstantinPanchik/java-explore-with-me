package ru.practicum.mainservice.user.dto;

import ru.practicum.mainservice.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserShortDto toShortDto(User user) {

        return new UserShortDto(user.getId(), user.getName());
    }

    public static UserAdminDto toAdminDto(User user) {

        return UserAdminDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
    }

    public static UserSubscribeDto toSubscribeDto(User user) {

        List<UserShortDto> dtos = user
                .getSubscriptions()
                .stream()
                .map(UserMapper::toShortDto)
                .collect(Collectors.toList());

        return UserSubscribeDto.builder()
                .id(user.getId())
                .name(user.getName())
                .subscriptions(dtos)
                .build();
    }
}
