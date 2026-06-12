package com.example.schedule_api.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.example.schedule_api.dto.PageResponse;
import com.example.schedule_api.dto.ScheduleResponse;
import com.example.schedule_api.entity.Artist;
import com.example.schedule_api.entity.Category;
import com.example.schedule_api.entity.Schedule;
import com.example.schedule_api.entity.ScheduleStatus;
import com.example.schedule_api.repository.ArtistRepository;
import com.example.schedule_api.repository.CategoryRepository;
import com.example.schedule_api.repository.ScheduleRepository;

/**
 * 목록 검색 동작 검증 + 하이버네이트 SQL 로그로 N+1 발생 여부 관찰용.
 * (시드는 테스트 편의상 리포지토리로 직접 넣는다)
 */
@SpringBootTest
class ScheduleSearchTest {

    @Autowired ScheduleService scheduleService;
    @Autowired ScheduleRepository scheduleRepository;
    @Autowired ArtistRepository artistRepository;
    @Autowired CategoryRepository categoryRepository;

    private Long aespaId;
    private Long concertId;

    @BeforeEach
    void seed() {
        scheduleRepository.deleteAll();
        artistRepository.deleteAll();
        categoryRepository.deleteAll();

        Artist aespa = artistRepository.save(new Artist("aespa"));
        Artist riize = artistRepository.save(new Artist("RIIZE"));
        Artist nct = artistRepository.save(new Artist("NCT WISH"));
        aespaId = aespa.getId();

        Category music = categoryRepository.save(new Category("음악방송"));
        Category concert = categoryRepository.save(new Category("콘서트"));
        concertId = concert.getId();

        LocalDateTime base = LocalDateTime.of(2026, 7, 1, 18, 0);
        scheduleRepository.saveAll(List.of(
                new Schedule("뮤직뱅크", base, "KBS", ScheduleStatus.SCHEDULED, null, aespa, music),
                new Schedule("단독 콘서트", base.plusDays(1), "KSPO DOME", ScheduleStatus.SCHEDULED, null, aespa, concert),
                new Schedule("인기가요", base.plusDays(2), "SBS", ScheduleStatus.SCHEDULED, null, riize, music),
                new Schedule("월드투어 서울", base.plusDays(3), "고척돔", ScheduleStatus.SCHEDULED, null, riize, concert),
                new Schedule("쇼! 음악중심", base.plusDays(4), "MBC", ScheduleStatus.SCHEDULED, null, nct, music),
                new Schedule("팬콘", base.plusDays(5), "잠실실내체육관", ScheduleStatus.SCHEDULED, null, nct, concert)
        ));
    }

    @Test
    void 전체_목록_조회() {
        System.out.println(">>>>> [전체 목록 조회] 시작");
        PageResponse<ScheduleResponse> result =
                scheduleService.search(null, null, PageRequest.of(0, 10, Sort.by("scheduleDate")));
        System.out.println(">>>>> [전체 목록 조회] 끝");

        assertThat(result.content()).hasSize(6);
        assertThat(result.totalElements()).isEqualTo(6);
        assertThat(result.content().get(0).title()).isEqualTo("뮤직뱅크");
    }

    @Test
    void artistId_categoryId_필터() {
        PageResponse<ScheduleResponse> result =
                scheduleService.search(aespaId, concertId, PageRequest.of(0, 10, Sort.by("scheduleDate")));

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).title()).isEqualTo("단독 콘서트");
        assertThat(result.content().get(0).artistName()).isEqualTo("aespa");
        assertThat(result.content().get(0).categoryName()).isEqualTo("콘서트");
    }

    @Test
    void 페이징_적용() {
        PageResponse<ScheduleResponse> result =
                scheduleService.search(null, null, PageRequest.of(1, 4, Sort.by("scheduleDate")));

        assertThat(result.content()).hasSize(2);
        assertThat(result.totalPages()).isEqualTo(2);
        assertThat(result.page()).isEqualTo(1);
    }
}
