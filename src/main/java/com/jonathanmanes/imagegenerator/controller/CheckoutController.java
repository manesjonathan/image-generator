package com.jonathanmanes.imagegenerator.controller;

import com.jonathanmanes.imagegenerator.service.StripeService;
import com.jonathanmanes.imagegenerator.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class CheckoutController {
    @Value("${stripe.secret.key}")
    private String apiKey;

    private final StripeService stripeService;
    private final UserService userService;

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
        String email = stripeService.extractEmailFromChargeEvent(json);
        userService.refillUser(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
