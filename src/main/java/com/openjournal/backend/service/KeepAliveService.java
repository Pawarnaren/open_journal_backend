package com.openjournal.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KeepAliveService {
    private static final Logger logger = LoggerFactory.getLogger(KeepAliveService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String PING_URL = "https://open-journal-backend.onrender.com/test";

    // Pings every 1 minute (60,000 milliseconds)
    @Scheduled(fixedRate = 2000)
    public void keepAlive() {
        try {
            String response = restTemplate.getForObject(PING_URL, String.class);
            logger.info("Self-ping successful: {}", response);
        } catch (Exception e) {
            logger.error("Self-ping failed: {}", e.getMessage());
        }
    }
}
