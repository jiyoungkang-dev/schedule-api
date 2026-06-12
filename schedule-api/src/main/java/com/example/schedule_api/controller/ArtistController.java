package com.example.schedule_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.schedule_api.dto.ArtistCreateRequest;
import com.example.schedule_api.dto.ArtistResponse;
import com.example.schedule_api.service.ArtistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @PostMapping
    public ResponseEntity<ArtistResponse> create(@Valid @RequestBody ArtistCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ArtistResponse>> findAll() {
        return ResponseEntity.ok(artistService.findAll());
    }
}
