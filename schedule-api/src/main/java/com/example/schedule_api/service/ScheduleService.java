package com.example.schedule_api.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.schedule_api.dto.PageResponse;
import com.example.schedule_api.dto.ScheduleCreateRequest;
import com.example.schedule_api.dto.ScheduleResponse;
import com.example.schedule_api.dto.ScheduleUpdateRequest;
import com.example.schedule_api.entity.Artist;
import com.example.schedule_api.entity.Category;
import com.example.schedule_api.entity.Schedule;
import com.example.schedule_api.exception.NotFoundException;
import com.example.schedule_api.repository.ArtistRepository;
import com.example.schedule_api.repository.CategoryRepository;
import com.example.schedule_api.repository.ScheduleRepository;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ArtistRepository artistRepository;
    private final CategoryRepository categoryRepository;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           ArtistRepository artistRepository,
                           CategoryRepository categoryRepository) {
        this.scheduleRepository = scheduleRepository;
        this.artistRepository = artistRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ScheduleResponse create(ScheduleCreateRequest request) {
        Artist artist = getArtist(request.artistId());
        Category category = getCategory(request.categoryId());

        Schedule schedule = scheduleRepository.save(new Schedule(
                request.title(),
                request.scheduleDate(),
                request.location(),
                request.status(),
                request.description(),
                artist,
                category
        ));
        return ScheduleResponse.from(schedule);
    }

    public ScheduleResponse findById(Long id) {
        return ScheduleResponse.from(getSchedule(id));
    }

    /** 목록 검색. artistId / categoryId 는 선택적 필터. */
    public PageResponse<ScheduleResponse> search(Long artistId, Long categoryId, Pageable pageable) {
        return PageResponse.from(
                scheduleRepository.search(artistId, categoryId, pageable)
                        .map(ScheduleResponse::from)
        );
    }

    @Transactional
    public ScheduleResponse update(Long id, ScheduleUpdateRequest request) {
        Schedule schedule = getSchedule(id);
        Artist artist = getArtist(request.artistId());
        Category category = getCategory(request.categoryId());

        schedule.update(
                request.title(),
                request.scheduleDate(),
                request.location(),
                request.status(),
                request.description(),
                artist,
                category
        );
        return ScheduleResponse.from(schedule);
    }

    @Transactional
    public void delete(Long id) {
        Schedule schedule = getSchedule(id);
        scheduleRepository.delete(schedule);
    }

    /** artist, category 를 fetch join 으로 함께 조회. 없으면 404. */
    private Schedule getSchedule(Long id) {
        return scheduleRepository.findByIdWithArtistAndCategory(id)
                .orElseThrow(() -> new NotFoundException("일정을 찾을 수 없습니다. id=" + id));
    }

    private Artist getArtist(Long artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new NotFoundException("아티스트를 찾을 수 없습니다. id=" + artistId));
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다. id=" + categoryId));
    }
}
