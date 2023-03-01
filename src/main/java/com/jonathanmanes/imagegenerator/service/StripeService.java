package com.jonathanmanes.imagegenerator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    private final ObjectMapper objectMapper;

    public StripeService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String extractEmailFromChargeEvent(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            JsonNode billingDetailsNode = node.get("data").get("object").get("charges").get("data").get(0).get("billing_details");
            return billingDetailsNode.get("email").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error extracting email from Stripe charge event JSON", e);
        }
    }
}
