package com.artistbooking.BookArtist.controller;


import com.artistbooking.BookArtist.dPayload.request.InitializePaymentDto;
import com.artistbooking.BookArtist.service.PaystackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {

    @Autowired
    private PaystackService paystackService;


    @PostMapping(path = "/initializePayment")
    public ResponseEntity<?> initializePayment(@RequestBody @Valid InitializePaymentDto initializePaymentDto) throws Exception {

        return new ResponseEntity<>(paystackService.initializePayment(initializePaymentDto), HttpStatus.OK);
    }


    @GetMapping("/verifypayment/{reference}/{plan}/{id}")
    public ResponseEntity<?> paymentVerification(@PathVariable(value = "reference") String reference,
                                                           @PathVariable(value = "plan") String plan,
                                                           @PathVariable(value = "id") Long id) throws Exception {
        if (reference.isEmpty() || plan.isEmpty()) {
            throw new Exception("reference, plan and id must be provided in path");
        }
       return new ResponseEntity<>(paystackService.paymentVerification(reference, plan, id),HttpStatus.OK);
    }


    @GetMapping(path = "/getAllPayments")
    public ResponseEntity<?> paymentHistory() throws Exception {

        return new ResponseEntity<>(paystackService.getAllPayments(), HttpStatus.OK);
    }
}
