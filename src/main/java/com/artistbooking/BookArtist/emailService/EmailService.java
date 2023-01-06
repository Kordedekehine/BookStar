package com.artistbooking.BookArtist.emailService;


import com.artistbooking.BookArtist.model.Client;
import jakarta.mail.MessagingException;

public interface EmailService {

    void sendRegistrationSuccessfulEmail(Client client)throws MessagingException;

    void sendBookingSuccessfulEmail(Client client)throws MessagingException;

    void sendSubscriptionSuccessfulEmail(Client client)throws MessagingException;

}
