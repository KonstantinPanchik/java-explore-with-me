package ru.practicum.mainservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserShortDto {

    Long id;

    String name;
}
