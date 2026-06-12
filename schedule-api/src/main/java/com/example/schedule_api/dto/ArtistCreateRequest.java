package com.example.schedule_api.dto;

import jakarta.validation.constraints.NotBlank;

public record ArtistCreateRequest(
        @NotBlank String name
) {
}
