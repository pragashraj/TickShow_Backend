package com.tickshow.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/landingPage/")
public class LandingPageController {
    private static final Logger log = LoggerFactory.getLogger(LandingPageController.class);

    @GetMapping("get-landingPage-contents")
    public ResponseEntity<?> getContents() {
        try {
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error("Unable to get contents, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}