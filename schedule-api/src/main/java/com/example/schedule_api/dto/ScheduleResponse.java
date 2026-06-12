package com.example.schedule_api.dto;

import java.time.LocalDateTime;

import com.example.schedule_api.entity.Schedule;
import com.example.schedule_api.entity.ScheduleStatus;

/**
 * 연관 엔티티(artist, category)는 객체 그대로가 아니라
 * id/name 평탄화 필드로만 노출한다. (엔티티 직렬화·지연로딩 예외 방지)
 */
public record ScheduleResponse(
        Long id,
        String title,
        LocalDateTime scheduleDate,
        String location,
        ScheduleStatus status,
        String description,
        Long artistId,
        String artistName,
        Long categoryId,
        String categoryName
) {
    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getScheduleDate(),
                schedule.getLocation(),
                schedule.getStatus(),
                schedule.getDescription(),
                schedule.getArtist().getId(),
                schedule.getArtist().getName(),
                schedule.getCategory().getId(),
                schedule.getCategory().getName()
        );
    }
}
