package com.artistbooking.BookArtist.dPayload.response;

import com.artistbooking.BookArtist.model.Handles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtisteResponseDto {

    private String name;

    private String image;

    private String aboutStar;

    private String currentAppearanceCharges;

    private String currentPerformanceCharges;

    private String location;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private Handles handles;
}
