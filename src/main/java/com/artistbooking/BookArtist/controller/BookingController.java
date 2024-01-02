package com.artistbooking.BookArtist.controller;

import com.artistbooking.BookArtist.dPayload.request.BookingDto;
import com.artistbooking.BookArtist.dPayload.request.FilterBookingDto;
import com.artistbooking.BookArtist.exception.*;
import com.artistbooking.BookArtist.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/booking")
public class BookingController {

     @Autowired
     private BookService bookService;


    @PostMapping (value = "/initiateBooking/{artisteId}")
    public ResponseEntity<?> initiateBooking(@PathVariable Long artisteId, @RequestBody BookingDto bookingDto) throws GeneralServiceException, NotFoundException, UserNotFoundException, RestrictedAccessException {
        return new ResponseEntity<>(bookService.initiateBooking(artisteId, bookingDto), HttpStatus.OK);
    }

    @PostMapping (value = "/verifyPayment")
    public ResponseEntity<?> initiateBooking(@RequestParam String paymentReference,@RequestParam String bookId) throws GeneralServiceException, NotFoundException {
        return new ResponseEntity<>(bookService.verifyPaymentAndConfirmBooking(paymentReference, bookId), HttpStatus.OK);
    }

    //MANAGER DOCUMENT EXTERNAL BOOKING TO AVOID CLASH
    @PostMapping (value = "/documentBooking")
    public ResponseEntity<?> documentBooking(@PathVariable Long artisteId, @RequestBody BookingDto bookingDto) throws GeneralServiceException, NotFoundException, RestrictedToManagerException, ManagerNotFoundException {
        return new ResponseEntity<>(bookService.documentBooking(artisteId, bookingDto), HttpStatus.OK);
    }

    @DeleteMapping (value = "/deleteBooking{bookingId}")
    public ResponseEntity<?> deleteUnverifiedPaymentBooking(@PathVariable Long bookingId) throws GeneralServiceException, NotFoundException, RestrictedAccessException, RestrictedToManagerException, ManagerNotFoundException {
        return new ResponseEntity<>(bookService.deleteUnverifiedPaymentBooking(bookingId), HttpStatus.OK);
    }

    @PostMapping("/filter-all-bookings")
    public ResponseEntity<?> searchFilter(@RequestParam(value = "page",defaultValue = "1") int page,
                                             @RequestParam(value = "size",defaultValue = "3") int size, @RequestBody FilterBookingDto filterBookingDto) {

            return new ResponseEntity<>(bookService.filterBookings(page,size,filterBookingDto), HttpStatus.OK);
    }
}
