package com.artistbooking.BookArtist.dPayload.response;

import com.artistbooking.BookArtist.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDto {

    private Long artisteId;

    private String bookingDate;

    private String artisteName;

    private BookingStatus bookingStatus;

    private String booked_verification;

    private String location;

    private String event;

    private String detailsOfArtisteInvite;

    private String expectations;

    private Boolean isProcessed;

}
