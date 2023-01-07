package com.artistbooking.BookArtist.controller;

import com.artistbooking.BookArtist.model.Client;
import com.artistbooking.BookArtist.repository.ClientRepository;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSubscriptionCollection;
import com.stripe.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class CancelBooking {

    @Autowired
    ClientRepository clientRepository;

    @RequestMapping(value="", method= RequestMethod.GET)
    public String cancelGet (@RequestParam String email, ModelMap model)
    {
        model.put("email", email);
        return "cancel";
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public @ResponseBody String cancelPost (@RequestParam String email, ModelMap model) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException
    {
        Set<Client> clients = clientRepository.findByEmail(email);

        Optional<Client> clientOpt = clients.stream().filter(c -> !ObjectUtils.isEmpty(c.getStripeId())).findFirst();

        if (clientOpt.isPresent())
        {
            Client client = clientOpt.get();
            Stripe.apiKey = "sk_test_51M0RttLgcmCoDSKxnrVZMBNyX7YUEQnQxSQfUz4IfPdcoHYU92EvNdBdtsT3Pw9RDiWvpRdelaeCHpIiEzPRqJdd00msgJoUjh";

            Customer stripeCustomer = Customer.retrieve(client.getStripeId());

            CustomerSubscriptionCollection subscriptionsCollection = stripeCustomer.getSubscriptions();

            List<Subscription> subscriptions = subscriptionsCollection.getData();

            subscriptions.stream().forEach(subscription -> {
                Map<String, Object> params = new HashMap<>();
                params.put("at_period_end", true);

                try
                {
                    subscription.cancel(params);
                } catch (AuthenticationException | InvalidRequestException
                         | APIConnectionException | CardException | APIException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }
        // cancel the stripe recurring billing
        System.out.println("we're inside of the cancel post method, and the email address is: " + email);
        return "{}";
    }
}
