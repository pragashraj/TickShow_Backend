package com.tickshow.backend.controller;

import com.tickshow.backend.repository.ContactRepository;
import com.tickshow.backend.request.SendMessageRequest;
import com.tickshow.backend.response.ApiResponse;
import com.tickshow.backend.usecase.SendMessageUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/contact/")
public class ContactController {
    private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    private final ContactRepository contactRepository;

    @Autowired
    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @PostMapping("send-message")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest request) {
        try {
            SendMessageUseCase useCase = new SendMessageUseCase(contactRepository, request);
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            log.error("Unable to send user message, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}