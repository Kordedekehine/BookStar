package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.Artiste;
import com.artistbooking.BookArtist.model.Booking;
import com.artistbooking.BookArtist.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByBookingDateAndArtiste(LocalDate bookingDate, Artiste artiste);

    boolean existsByUserAndBookingDate(UserEntity user, LocalDate bookingDate);

    Optional<Booking> findByBookedVerification(String bookedVerification);

    Optional<Booking> findByBookingDate(LocalDate bookingDate);
}
