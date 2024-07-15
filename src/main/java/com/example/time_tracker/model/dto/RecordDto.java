package com.example.time_tracker.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordDto {

    @Positive
    private Long id;

    private String description;

    @NotNull(message = "Start time should not be empty")
    @Past(message = "Start time should be less than current time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE)
    private Date startTime;

    @NotNull(message = "End time should not be empty")
    @PastOrPresent(message = "End time should not be greater than current time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE)
    private Date endTime;
}
