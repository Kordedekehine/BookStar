package com.artistbooking.BookArtist.dPayload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterBookingDto {

    private LocalDate bookingDate;

    private String event;

    private String ArtisteName;

    private String artisteLocation;
}
