package com.artistbooking.BookArtist.emailService;


import com.artistbooking.BookArtist.model.Client;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;


@Service
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender javaMailSender;


    private final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    private final String BOOKING_ERROR_MESSAGE = "Booking Not Successful";

    private final String REGISTRATION_ERROR_MESSAGE = "Registration Not Successful";

    private final String SUBSCRIPTION_ERROR_MESSAGE = "Subscription Not Successful";



    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendRegistrationSuccessfulEmail(Client client) throws MessagingException {

        simpleMailMessage.setTo(client.getEmail());
        simpleMailMessage.setSubject("Amukunmeko Entertainment World");
        simpleMailMessage.setFrom("djava8410@gmail.com");
        String template = "Dear [[name]],\n"
                + "Welcome to the world of the moon among stars-SALAMI KEHINDE KOREDE.\n"
                +" Known by the stage name AKOREDETHEFIRST"
                + "[[code]]\n"
                + "Thank you.\n"
                + "Amukunmeko Entertainment team";
        template = template.replace("[[name]]", client.getName());
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(REGISTRATION_ERROR_MESSAGE));
        }
    }

    @Override
    public void sendBookingSuccessfulEmail(Client client) throws MessagingException {

        simpleMailMessage.setTo(client.getEmail());
        simpleMailMessage.setSubject("Booking Successful");
        simpleMailMessage.setFrom("djava8410@gmail.com");
        String template = "Dear [[name]],\n"
                + "Be ready to be part of my rollercoaster journey.\n"
                + "NOW LET'S HAVE FUN!!!.\n"
                + "[[code]]\n"
                + "Thank you.\n"
                + "Amukunmeko Entertainment team";
        template = template.replace("[[name]]", client.getName());
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(BOOKING_ERROR_MESSAGE));
        }
    }

    @Override
    public void sendSubscriptionSuccessfulEmail(Client client) throws MessagingException {

        simpleMailMessage.setTo(client.getEmail());
        simpleMailMessage.setSubject("Successful subscription");
        simpleMailMessage.setFrom("djava8410@gmail.com");
        String template = "Dear [[name]],\n"
                + "Now you have unlimited access to the Star private story.\n"
                + "NOW LET'S HAVE FUN!!!.\n"
                + "[[code]]\n"
                + "Thank you.\n"
                + "Amukunmeko Entertainment team";
        template = template.replace("[[name]]", client.getName());
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(SUBSCRIPTION_ERROR_MESSAGE));
        }
    }

}


