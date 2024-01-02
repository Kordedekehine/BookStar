package com.artistbooking.BookArtist.service;

import com.artistbooking.BookArtist.dPayload.request.BookingDto;
import com.artistbooking.BookArtist.dPayload.request.FilterBookingDto;
import com.artistbooking.BookArtist.dPayload.response.BookingListResponseDto;
import com.artistbooking.BookArtist.dPayload.response.BookingResponseDto;
import com.artistbooking.BookArtist.exception.*;

public interface BookService {
//,BookingDto bookingDto
    BookingResponseDto initiateBooking(Long artisteId,BookingDto bookingDto) throws GeneralServiceException, NotFoundException, RestrictedAccessException, UserNotFoundException;


    BookingResponseDto verifyPaymentAndConfirmBooking(String paymentReference, String bookId) throws GeneralServiceException;

    /**
     * THIS METHOD IS FOR MANAGER ONLY. THIS IS WHEN THEY ARE BOOKED OUTSIDE THE PLATFORM AND THEY NEED TO RECORD
     * THE DATE SO NO PLATFORM USERS CAN BOOK THE ARTISTE ON THAT SAME DAY--A FIXED DATE
     */
    BookingResponseDto documentBooking(Long artisteId,BookingDto bookingDto) throws GeneralServiceException, NotFoundException, RestrictedToManagerException, ManagerNotFoundException;

    String deleteUnverifiedPaymentBooking(Long bookingId) throws RestrictedAccessException, NotFoundException, RestrictedToManagerException, ManagerNotFoundException;


   BookingListResponseDto filterBookings(int page, int size, FilterBookingDto filterBookingDto);
}
