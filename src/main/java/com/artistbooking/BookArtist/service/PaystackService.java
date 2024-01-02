package com.artistbooking.BookArtist.service;


import com.artistbooking.BookArtist.dPayload.request.CreatePlanDto;
import com.artistbooking.BookArtist.dPayload.request.InitializePaymentDto;
import com.artistbooking.BookArtist.dPayload.request.PaymentVerificationDto;
import com.artistbooking.BookArtist.dPayload.response.CreatePlanResponse;
import com.artistbooking.BookArtist.dPayload.response.InitializePaymentResponse;
import com.artistbooking.BookArtist.dPayload.response.PaymentVerificationResponse;
import com.artistbooking.BookArtist.exception.NotFoundException;
import com.artistbooking.BookArtist.exception.RestrictedAccessException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface PaystackService {
    CreatePlanResponse createPlan(CreatePlanDto createPlanDto) throws Exception;
    InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto) throws NotFoundException, RestrictedAccessException, UnsupportedEncodingException;
    PaymentVerificationResponse paymentVerification(String reference, String plan, Long id) throws Exception;

   // List<PaymentVerificationDto> getAllPayments();

    List<PaymentVerificationDto> getAllPayments() throws Exception;
}