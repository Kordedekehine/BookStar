package com.artistbooking.BookArtist.model;

import com.artistbooking.BookArtist.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "artiste_id")
    private Artiste artiste;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private String event;

    private String detailsOfArtisteInvite;

    private String expectations;

    @Column(name = "booked_verification_id")
    private String bookedVerification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    private Boolean isProcessed;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "booked_date", updatable = false, nullable = false)
    private LocalDate bookingDate;


}
