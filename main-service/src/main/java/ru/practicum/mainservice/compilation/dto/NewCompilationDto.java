package ru.practicum.mainservice.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    @NotNull
    private Boolean pinned;

    @NotBlank
    private String title;

    private Set<Long> events;

}
