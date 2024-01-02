package com.artistbooking.BookArtist.dPayload.request;

import com.artistbooking.BookArtist.model.Artiste;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {

    private LocalDate bookingDate;

    private String event;

    private String detailsOfArtisteInvite;


    private String expectations;

}
