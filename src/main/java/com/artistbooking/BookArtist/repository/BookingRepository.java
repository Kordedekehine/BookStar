package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.Artiste;
import com.artistbooking.BookArtist.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByBookingDateAndArtiste(LocalDateTime bookingDate, Artiste artiste);


}
