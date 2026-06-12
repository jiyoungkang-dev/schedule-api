package com.example.schedule_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.schedule_api.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * 단건 조회 시 artist, category 를 fetch join 으로 함께 가져온다.
     * (응답 DTO 변환 시 지연로딩 추가 쿼리 방지)
     */
    @Query("select s from Schedule s join fetch s.artist join fetch s.category where s.id = :id")
    Optional<Schedule> findByIdWithArtistAndCategory(@Param("id") Long id);
}
