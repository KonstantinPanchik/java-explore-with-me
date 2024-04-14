package ru.practicum.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.user.dto.UserShortDto;

import java.time.LocalDateTime;
@Data
@Builder
public class EventShortDto {

    Long id;

    String annotation;

    Category category;

    int confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    UserShortDto initiator;

    boolean paid;

    String title;

    int views;
}
