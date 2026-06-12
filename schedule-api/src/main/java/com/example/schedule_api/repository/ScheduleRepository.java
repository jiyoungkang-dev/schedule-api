package com.example.schedule_api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

    /**
     * 목록 검색. artistId / categoryId 는 선택적 필터 (null 이면 조건 무시).
     * artist, category 는 @EntityGraph 로 본문 쿼리에서 left join 으로 함께 조회 (N+1 방지).
     * count 쿼리에는 적용되지 않으므로 페이징 카운트는 단독 쿼리로 나간다.
     */
    @EntityGraph(attributePaths = {"artist", "category"})
    @Query("""
            select s from Schedule s
            where (:artistId is null or s.artist.id = :artistId)
              and (:categoryId is null or s.category.id = :categoryId)
            """)
    Page<Schedule> search(@Param("artistId") Long artistId,
                          @Param("categoryId") Long categoryId,
                          Pageable pageable);
}
