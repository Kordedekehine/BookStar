package com.artistbooking.BookArtist.dPayload.request;

import com.artistbooking.BookArtist.model.Handles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtisteDto {

    private String name;

    private String image;

    private String aboutStar;

    private String location;

    private String currentAppearanceCharges;

    private String currentPerformanceCharges;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private Handles handles;
}
