package com.example.schedule_api.dto;

import java.time.LocalDateTime;

import com.example.schedule_api.entity.ScheduleStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ScheduleCreateRequest(
        @NotNull Long artistId,
        @NotNull Long categoryId,
        @NotBlank String title,
        @NotNull LocalDateTime scheduleDate,
        String location,
        @NotNull ScheduleStatus status,
        String description
) {
}
