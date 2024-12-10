package com.epi.notificationservice.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.epi.notificationservice.request.SendNotificationRequest;
import com.epi.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "notificationTopic5", groupId = "notification")
    public void consume(final String message) {
        try {
            SendNotificationRequest request = objectMapper.readValue(message, SendNotificationRequest.class);
            log.info("Deserialized message: {}", request);
            notificationService.save(request);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message: {}", message, e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}