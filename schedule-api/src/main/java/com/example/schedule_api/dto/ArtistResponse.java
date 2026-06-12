package com.example.schedule_api.dto;

import com.example.schedule_api.entity.Artist;

public record ArtistResponse(
        Long id,
        String name
) {
    public static ArtistResponse from(Artist artist) {
        return new ArtistResponse(artist.getId(), artist.getName());
    }
}
