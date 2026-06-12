package com.example.schedule_api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    /** 일정 일시 (날짜 + 시간) */
    @Column(nullable = false)
    private LocalDateTime scheduleDate;

    private String location;

    /** 일정 상태: 예정(SCHEDULED) / 완료(COMPLETED). 문자열로 저장 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleStatus status;

    private String description;

    /**
     * 아티스트와의 N:1 연관관계.
     * 하나의 아티스트(Artist)는 여러 개의 일정(Schedule)을 가질 수 있고,
     * 각 일정은 정확히 한 명의 아티스트에 속한다. (이 일정이 "누구의" 일정인지)
     * 지연로딩(LAZY): Schedule 을 조회할 때 Artist 는 실제로 사용하는 시점에만 조회한다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    /**
     * 카테고리와의 N:1 연관관계.
     * 하나의 카테고리(Category)는 여러 개의 일정(Schedule)을 가질 수 있고,
     * 각 일정은 정확히 하나의 카테고리에 속한다. (이 일정이 "무슨 종류"인지: 음악방송/콘서트/팬미팅 등)
     * 지연로딩(LAZY): Schedule 을 조회할 때 Category 는 실제로 사용하는 시점에만 조회한다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    protected Schedule() {
    }

    public Schedule(String title, LocalDateTime scheduleDate, String location,
                    ScheduleStatus status, String description, Artist artist, Category category) {
        this.title = title;
        this.scheduleDate = scheduleDate;
        this.location = location;
        this.status = status;
        this.description = description;
        this.artist = artist;
        this.category = category;
    }

    /** 일정 내용 전체 수정. 더티 체킹으로 트랜잭션 커밋 시 UPDATE 가 나간다. */
    public void update(String title, LocalDateTime scheduleDate, String location,
                       ScheduleStatus status, String description, Artist artist, Category category) {
        this.title = title;
        this.scheduleDate = scheduleDate;
        this.location = location;
        this.status = status;
        this.description = description;
        this.artist = artist;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getScheduleDate() {
        return scheduleDate;
    }

    public String getLocation() {
        return location;
    }

    public ScheduleStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Artist getArtist() {
        return artist;
    }

    public Category getCategory() {
        return category;
    }
}
