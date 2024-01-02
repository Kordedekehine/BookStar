package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.Booking;
import com.artistbooking.BookArtist.model.PaymentPaystack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaystackPaymentRepository extends JpaRepository<PaymentPaystack, Long> {

   Optional<PaymentPaystack> findByReference(String paymentReference);


   Optional<PaymentPaystack> deleteByReference(String paymentReference);

   Optional<Booking> deleteByPaymentVerification(String paymentVerification);
}
