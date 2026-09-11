package com.openjournal.backend.controller;

import com.openjournal.backend.model.User;
import com.openjournal.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/test")
    public String test() {
        return "deployed successfully";
    }

    @GetMapping("/fetch")
    public ResponseEntity<Map<String, Object>> fetchUserNames() {
        Map<String, Object> body = new LinkedHashMap<>();
        try {
            List<String> names = userRepository.findAll().stream()
                    .map(User::getUsername)
                    .toList();

            body.put("ok", true);
            body.put("db", "connected");
            body.put("count", names.size());
            body.put("names", names);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            log.error("DB fetch failed", e);
            body.put("ok", false);
            body.put("db", "error");
            body.put("errorType", e.getClass().getSimpleName());
            body.put("message", e.getMessage());
            Throwable cause = e.getCause();
            if (cause != null) {
                body.put("cause", cause.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }
}
