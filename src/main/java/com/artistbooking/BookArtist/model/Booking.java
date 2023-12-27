package com.artistbooking.BookArtist.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
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


    @CreationTimestamp
    @Column(name = "booking", updatable = false, nullable = false)
    private LocalDateTime bookingDate;


}
