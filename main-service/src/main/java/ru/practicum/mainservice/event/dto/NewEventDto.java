package ru.practicum.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.location.Location;
import ru.practicum.mainservice.validator.FutureTwoHour;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotNull
    @Size(max = 2000, min = 20)
    private String annotation;

    @NotNull
    private Long category;

    @NotNull
    @Size(max = 7000, min = 20)
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureTwoHour
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private Boolean paid; //false по умолчанию

    private int participantLimit;

    private Boolean requestModeration;//true по умо

    @NotNull
    @Size(max = 120, min = 3)
    private String title;


}
