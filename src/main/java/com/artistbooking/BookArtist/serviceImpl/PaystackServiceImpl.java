package com.artistbooking.BookArtist.serviceImpl;

import com.artistbooking.BookArtist.dPayload.request.InitializePaymentDto;
import com.artistbooking.BookArtist.dPayload.request.PaymentVerificationDto;
import com.artistbooking.BookArtist.dPayload.response.InitializePaymentResponse;
import com.artistbooking.BookArtist.dPayload.response.PaymentVerificationResponse;
import com.artistbooking.BookArtist.enums.PricingPlanType;
import com.artistbooking.BookArtist.model.PaymentPaystack;
import com.artistbooking.BookArtist.model.UserEntity;
import com.artistbooking.BookArtist.repository.BookingRepository;
import com.artistbooking.BookArtist.repository.PaystackPaymentRepository;
import com.artistbooking.BookArtist.repository.UserRepository;
import com.artistbooking.BookArtist.service.PaystackService;
import com.artistbooking.BookArtist.util.UniquePaymentIdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.artistbooking.BookArtist.constant.APIConstants.*;

@Service
@Slf4j
public class PaystackServiceImpl implements PaystackService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaystackPaymentRepository paystackPaymentRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Value("${paystack.secret.key}")
    private String paystackSecretKey;


    @Override
    public InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto) {
        InitializePaymentResponse initializePaymentResponse = null;

        try {
            Gson gson = new Gson();
            StringEntity postingString = new StringEntity(gson.toJson(initializePaymentDto));
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(PAYSTACK_INITIALIZE_PAY);
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer " + paystackSecretKey);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == STATUS_CODE_OK) {

                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } else {
                throw new Exception("Paystack is unable to initialize payment at the moment");
            }

            ObjectMapper mapper = new ObjectMapper();
            initializePaymentResponse = mapper.readValue(result.toString(), InitializePaymentResponse.class);
        } catch(Throwable ex) {
            ex.printStackTrace();
        }
        return initializePaymentResponse;
    }


    @Override
    public PaymentVerificationResponse paymentVerification(String reference, String plan, Long id) throws Exception {
        PaymentVerificationResponse paymentVerificationResponse = null;
        PaymentPaystack paymentPaystack = null;

        try {
            HttpUriRequest request = RequestBuilder
                    .get(PAYSTACK_VERIFY + reference)
                    .addHeader("Content-type", "application/json")
                    .addHeader("Authorization", "Bearer " + paystackSecretKey)
                    .build();

            StringBuilder result = new StringBuilder();

            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse response = client.execute(request);
                 BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {

                if (response.getStatusLine().getStatusCode() == STATUS_CODE_OK) {
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                } else {
                    throw new Exception("Paystack is unable to verify payment at the moment. Status code: " +
                            response.getStatusLine().getStatusCode());
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            paymentVerificationResponse = mapper.readValue(result.toString(), PaymentVerificationResponse.class);

            if (paymentVerificationResponse == null || !"success".equals(paymentVerificationResponse.getData().getStatus())) {
                throw new Exception("An error occurred during payment verification. Response: " + result.toString());
            }

            UserEntity appUser = userRepository.getById(id);
            if (appUser == null) {
                throw new Exception("User with ID " + id + " not found.");
            }

            PricingPlanType pricingPlanType = PricingPlanType.valueOf(plan.toUpperCase());

            paymentPaystack = PaymentPaystack.builder()
                    .user(appUser)
                    .reference(paymentVerificationResponse.getData().getReference())
                    .amount(paymentVerificationResponse.getData().getAmount())
                    .gatewayResponse(paymentVerificationResponse.getData().getGatewayResponse())
                    .paidAt(paymentVerificationResponse.getData().getPaidAt())
                    .createdAt(paymentVerificationResponse.getData().getCreatedAt())
                    .channel(paymentVerificationResponse.getData().getChannel())
                    .currency(paymentVerificationResponse.getData().getCurrency())
                    .ipAddress(paymentVerificationResponse.getData().getIpAddress())
                    .paymentVerification("BookStar " + UniquePaymentIdGenerator.generateId())
                    .pricingPlanType(pricingPlanType)
                    .createdOn(new Date())
                    .build();

        } catch (Exception ex) {
            // Log the exception for debugging purposes
            ex.printStackTrace();
            throw new Exception("Paystack verification failed. Error: " + ex.getMessage());
        }

        // Ensure that paymentPaystack is not null before saving
        if (paymentPaystack != null) {
            paystackPaymentRepository.save(paymentPaystack);
        } else {
            throw new Exception("PaymentPaystack object is null after verification.");
        }

        return paymentVerificationResponse;
    }

    @Override
    public List<PaymentVerificationDto> getAllPayments() {

        List<PaymentPaystack> paystackPayments = paystackPaymentRepository.findAll();


       return paystackPayments.stream().
               map(paymentPaystack -> PaymentVerificationDto.builder()
                       .amount(paymentPaystack.getAmount())
                       .paidAt(paymentPaystack.getPaidAt())
                       .gatewayResponse(paymentPaystack.getGatewayResponse())
                       .reference(paymentPaystack.getReference())
                       .channel(paymentPaystack.getChannel())
                       .pricingPlanType(paymentPaystack.getGatewayResponse())
                       .payment_verification(paymentPaystack.getPaymentVerification())
                       .currency(paymentPaystack.getCurrency()).build()).collect(Collectors.toList());

    }


}
