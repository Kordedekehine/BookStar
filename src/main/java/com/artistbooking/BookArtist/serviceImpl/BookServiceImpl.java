package com.artistbooking.BookArtist.serviceImpl;

import com.artistbooking.BookArtist.dPayload.request.BookingDto;
import com.artistbooking.BookArtist.dPayload.request.FilterBookingDto;
import com.artistbooking.BookArtist.dPayload.response.BookingListResponseDto;
import com.artistbooking.BookArtist.dPayload.response.BookingResponseDto;
import com.artistbooking.BookArtist.enums.BookingStatus;
import com.artistbooking.BookArtist.exception.*;
import com.artistbooking.BookArtist.model.*;
import com.artistbooking.BookArtist.repository.*;
import com.artistbooking.BookArtist.service.BookService;
import com.artistbooking.BookArtist.util.UniquePaymentIdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

     @Autowired
     private BookingRepository bookingRepository;

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private ManagerRepository managerRepository;

     @Autowired
     private HttpSession session;

     @Autowired
     private ArtisteRepository artisteRepository;

     @Autowired
     private PaystackPaymentRepository paymentRepository;

     @Autowired
     private ModelMapper modelMapper;

     //,BookingDto bookingDto
     @Transactional
     @Override
     public BookingResponseDto initiateBooking(Long artisteId, BookingDto bookingDto) throws GeneralServiceException, NotFoundException, RestrictedAccessException, UserNotFoundException {

         Long userId = (Long) session.getAttribute("userid");
         if (userId == null) {
             throw new RestrictedAccessException();
         }

         Optional<UserEntity> optionalUser = userRepository.findById(userId);
         if (optionalUser.isEmpty()) {
             throw new UserNotFoundException();
         }

         UserEntity user = optionalUser.get();

         LocalDate bookingDate = LocalDate.parse(bookingDto.getBookingDate().toString());  //toLocalDate();

         // Check if the booking is for the current day
         if (bookingDate.isEqual(LocalDate.now())) {
             throw new GeneralServiceException("Sorry! You cannot book artiste on the same day of the event");
         }

         // Check if the user has already booked on the same date
         if (hasBookedOnSameDate(user, bookingDate)) {
             throw new GeneralServiceException("Sorry! You cannot book artiste more than once on the same day");
         }

         // Retrieve the artist from the database
         Artiste artiste = artisteRepository.findById(artisteId)
                 .orElseThrow(() -> new GeneralServiceException("Artist not found"));

         // Check if the artist is already booked for the given date
         List<Booking> existingBookings = bookingRepository.findByBookingDateAndArtiste(bookingDate, artiste);

         if (!existingBookings.isEmpty()) {
             throw new GeneralServiceException("Artist is already booked for the given date");
         }

         Booking newBooking = new Booking();
         newBooking.setUser(user);
         newBooking.setArtiste(artiste);
         newBooking.setManager(artiste.getManager());
         newBooking.setBookingDate(bookingDate);
         newBooking.setBookedVerification("Booked " + UniquePaymentIdGenerator.generateId());
         newBooking.setBookingStatus(BookingStatus.valueOf("PENDING_PAYMENT"));
         newBooking.setEvent(bookingDto.getEvent());
         newBooking.setExpectations(bookingDto.getExpectations());
         newBooking.setDetailsOfArtisteInvite(bookingDto.getDetailsOfArtisteInvite());
         newBooking.setIsProcessed(false);

         bookingRepository.save(newBooking);

         //TODO implement a chat to the manager on the proposed booking only when manager seems fine with it
         //TODO  that the users can go on and make payment and finally confirm booking

//TODO but for now I will use mail--which is not ideal because all business conversations must be on the platform

         return BookingResponseDto.builder()
                 .bookingDate(String.valueOf(bookingDto.getBookingDate()))
                 .artisteId(artiste.getId())
                 .artisteName(artiste.getName())
                 .booked_verification(newBooking.getBookedVerification())
                 .bookingStatus(BookingStatus.valueOf(newBooking.getBookingStatus().toString()))
                 .location(artiste.getLocation())
                 .expectations(bookingDto.getExpectations())
                 .event(bookingDto.getEvent())
                 .detailsOfArtisteInvite(bookingDto.getDetailsOfArtisteInvite())
                 .isProcessed(false)
                 .build();
     }

    private boolean hasBookedOnSameDate(UserEntity user, LocalDate bookingDate) {
        return bookingRepository.existsByUserAndBookingDate(user, bookingDate);
    }

     @Transactional
     @Override
     public BookingResponseDto verifyPaymentAndConfirmBooking(String paymentReference, String bookId) throws GeneralServiceException {
          // Retrieve the payment reference
          PaymentPaystack paymentPaystack = paymentRepository.findByReference(paymentReference)
                  .orElseThrow(() -> new GeneralServiceException("Payment Reference cannot be retrieved " + paymentReference));


          Booking booking = bookingRepository.findByBookedVerification(bookId)
                  .orElseThrow(() -> new GeneralServiceException("Booking has never been initiated"));

          // Check if the booking is already confirmed
          if (BookingStatus.PAYMENT_CONFIRMED.equals(booking.getBookingStatus())) {
               throw new GeneralServiceException("Booking is already confirmed");
          }

          if (paymentPaystack.getPaymentVerification() == null &&
                  !paymentPaystack.getGatewayResponse().equalsIgnoreCase("success")){
               throw new  GeneralServiceException("Invalid Payment Verification");
          }


               // Check if the booking is already confirmed
               if ( BookingStatus.PAYMENT_CONFIRMED.equals(booking.getBookingStatus()) && !booking.equals(null)) {
                    throw new GeneralServiceException("Booking is already confirmed");
               }

               // Update booking status to PAYMENT_CONFIRMED
               booking.setIsProcessed(true);
               booking.setBookingStatus(BookingStatus.PAYMENT_CONFIRMED);
               bookingRepository.save(booking);


               return BookingResponseDto.builder()
                       .artisteId(booking.getArtiste().getId())
                       .event(booking.getEvent())
                       .artisteName(booking.getArtiste().getName())
                       .detailsOfArtisteInvite(booking.getDetailsOfArtisteInvite())
                       .expectations(booking.getExpectations())
                       .bookingStatus(BookingStatus.PAYMENT_CONFIRMED)
                       .isProcessed(true)
                       .bookingDate(String.valueOf(booking.getBookingDate()))
                       .booked_verification(booking.getBookedVerification())
                       .build();

     }

     @Override
     public BookingResponseDto documentBooking(Long artisteId, BookingDto bookingDto) throws GeneralServiceException, NotFoundException, RestrictedToManagerException, ManagerNotFoundException {

          Long managerId = (Long) session.getAttribute("managerid");
          if (managerId == null) {
               throw new RestrictedToManagerException();
          }

          Optional<Manager> optionalManager = managerRepository.findById(managerId);
          if (optionalManager.isEmpty())
          {
               throw new ManagerNotFoundException();
          }

         Manager manager = optionalManager.get();


         LocalDate bookingDate = LocalDate.parse(bookingDto.getBookingDate().toString());  //toLocalDate();

         // Check if the booking is for the current day
         if (bookingDate.isEqual(LocalDate.now())) {
             throw new GeneralServiceException("Sorry! You cannot book artiste on the same day of the event");
         }

         // Retrieve the artist from the database
         Artiste artiste = artisteRepository.findById(artisteId)
                 .orElseThrow(() -> new GeneralServiceException("Artist not found"));

         // Check if the artist is already booked for the given date
         List<Booking> existingBookings = bookingRepository.findByBookingDateAndArtiste(bookingDate, artiste);

         if (!existingBookings.isEmpty()) {
             throw new GeneralServiceException("Artist is already booked for the given date");
         }

         Booking newBooking = new Booking();
         newBooking.setArtiste(artiste);
         newBooking.setManager(manager);
         newBooking.setBookingDate(bookingDate);
         newBooking.setBookedVerification("Booked " + UniquePaymentIdGenerator.generateId());
         newBooking.setBookingStatus(BookingStatus.valueOf(BookingStatus.PAYMENT_CONFIRMED.toString()));
         newBooking.setEvent(bookingDto.getEvent());
         newBooking.setExpectations(bookingDto.getExpectations());
         newBooking.setDetailsOfArtisteInvite(bookingDto.getDetailsOfArtisteInvite());
         newBooking.setIsProcessed(true);

         bookingRepository.save(newBooking);

         return BookingResponseDto.builder()
                 .bookingDate(String.valueOf(bookingDto.getBookingDate()))
                 .artisteId(artiste.getId())
                 .artisteName(artiste.getName())
                 .booked_verification(newBooking.getBookedVerification())
                 .bookingStatus(BookingStatus.valueOf(newBooking.getBookingStatus().toString()))
                 .location(artiste.getLocation())
                 .expectations(bookingDto.getExpectations())
                 .event(bookingDto.getEvent())
                 .detailsOfArtisteInvite(bookingDto.getDetailsOfArtisteInvite())
                 .isProcessed(true)
                 .build();
     }

    @Override
    public String deleteUnverifiedPaymentBooking(Long bookingId) throws RestrictedAccessException, NotFoundException, RestrictedToManagerException, ManagerNotFoundException {

        Long managerId = (Long) session.getAttribute("managerid");
        if (managerId == null) {
            throw new RestrictedToManagerException();
        }

        Optional<Manager> optionalManager = managerRepository.findById(managerId);
        if (optionalManager.isEmpty())
        {
            throw new ManagerNotFoundException();
        }

        Manager manager = optionalManager.get();

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

        if (optionalBooking.isEmpty()){
            throw new NotFoundException();
        }

        Booking existingBooking = optionalBooking.get();

        bookingRepository.delete(existingBooking);

        return "Manager " + manager.getName() + " deleted the " + existingBooking.getEvent() + " event sheduled on " +
                existingBooking.getBookingDate();
    }

    @Override
    public BookingListResponseDto filterBookings(int page, int size, FilterBookingDto bookingDto) {


        //create pageable
        Pageable pageable = PageRequest.of((page - 1), size);

       BookingListResponseDto bookingListResponseDtos = new BookingListResponseDto();
        //find all return page
        Page<Booking> bookings = bookingRepository.findAll(pageable);
        //get total size of list

        List<Booking> bookingList = bookings.getContent();

        //by artiste name
        if(bookingDto.getArtisteName()!= null){
            bookingList= bookingList.stream().filter(booking -> booking.getArtiste().getName().
                    contains(booking.getArtiste().getName())).collect(Collectors.toList());
        }

        if(bookingDto.getEvent()!= null){
            bookingList= bookingList.stream().filter(booking -> booking.getEvent().
                    contains(booking.getEvent())).collect(Collectors.toList());
        }

        if(bookingDto.getBookingDate()!= null){
            bookingList= bookingList.stream().filter(booking -> booking.getBookingDate().toString().
                    contains(booking.getBookingDate().toString())).collect(Collectors.toList());
        }

        if(bookingDto.getArtisteLocation()!= null){
            bookingList= bookingList.stream().filter(booking -> booking.getArtiste().getLocation().
                    contains(booking.getArtiste().getLocation())).collect(Collectors.toList());
        }

        int totalSizeOfList = bookingList.size();
        //create a dto list for contents
        List<BookingResponseDto> bookingResponseDtoList = new ArrayList<>();
        for (Booking booking : bookingList) {
            //loop through contents
            BookingResponseDto bookingResponseDto = new BookingResponseDto();
            //map contents to dtos
            modelMapper.map(booking, bookingResponseDto);
            //add dto to dto list
            bookingResponseDtoList.add(bookingResponseDto);
        }

        //create response object

        //set data into response object
        bookingListResponseDtos.setBookingResponseDtoList(bookingResponseDtoList);
        //set data into response object
        bookingListResponseDtos.setSizeOfList(totalSizeOfList);

        return bookingListResponseDtos;
    }


}



