package com.tickshow.backend.usecase;

import com.tickshow.backend.model.entity.Contact;
import com.tickshow.backend.repository.ContactRepository;
import com.tickshow.backend.request.SendMessageRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@AllArgsConstructor
public class SendMessageUseCase {
    private static final Logger log = LoggerFactory.getLogger(SendMessageUseCase.class);

    private final ContactRepository contactRepository;
    private final SendMessageRequest request;

    public String execute() {
        Contact contact = Contact.builder()
                .name(request.getName())
                .email(request.getEmail())
                .subject(request.getSubject())
                .dateTime(LocalDateTime.now())
                .message(request.getMessage())
                .isReplied(false)
                .build();

        contactRepository.save(contact);

        log.info("A message from user {} to admin", request.getName());

        return "Message has been successfully sent";
    }
}