package com.example.schedule_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.schedule_api.dto.ArtistCreateRequest;
import com.example.schedule_api.dto.ArtistResponse;
import com.example.schedule_api.entity.Artist;
import com.example.schedule_api.repository.ArtistRepository;

@Service
@Transactional(readOnly = true)
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Transactional
    public ArtistResponse create(ArtistCreateRequest request) {
        Artist artist = artistRepository.save(new Artist(request.name()));
        return ArtistResponse.from(artist);
    }

    public List<ArtistResponse> findAll() {
        return artistRepository.findAll().stream()
                .map(ArtistResponse::from)
                .toList();
    }
}
