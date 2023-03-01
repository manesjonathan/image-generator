package com.jonathanmanes.imagegenerator.controller;

import com.google.gson.JsonElement;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class CheckoutController {
    @Value("${stripe.secret.key}")
    private String apiKey;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPayment(@RequestParam("amount") long amount) throws StripeException {
        Stripe.apiKey = apiKey;
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("eur")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build())
                        .build();

        // Create a PaymentIntent with the order amount and currency
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return new ResponseEntity<>(paymentIntent.getClientSecret(), HttpStatus.OK);
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> paymentSucceeded(@RequestBody String json) {
        Event event = Event.GSON.fromJson(json, Event.class);
        EventDataObjectDeserializer stripeObject = event.getDataObjectDeserializer();

        System.out.println(stripeObject.getRawJson());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
