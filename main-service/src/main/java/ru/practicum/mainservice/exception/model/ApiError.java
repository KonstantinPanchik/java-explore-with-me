package ru.practicum.mainservice.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    HttpStatus status;

    String reason;

    String message;

    @JsonFormat(pattern = "yyyy-mm-dd hh:MM:ss")
    LocalDateTime timestamp;
}
