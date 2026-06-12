package com.example.schedule_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.schedule_api.entity.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
