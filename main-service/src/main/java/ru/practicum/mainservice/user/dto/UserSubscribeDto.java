package ru.practicum.mainservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UserSubscribeDto {

    private Long id;

    private String name;

    private List<UserShortDto> subscriptions;

}
