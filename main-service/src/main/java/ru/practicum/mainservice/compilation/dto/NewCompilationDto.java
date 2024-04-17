package ru.practicum.mainservice.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {


    private Boolean pinned;

    @NotBlank(groups = OnCreate.class)
    @Size(min = 3, max = 50, groups = {OnCreate.class, OnUpdate.class})
    private String title;

    private Set<Long> events;

    public interface OnCreate {
    }

    public interface OnUpdate {
    }
}
