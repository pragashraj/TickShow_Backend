package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.entity.Contact;
import com.tickshow.backend.repository.ContactRepository;
import com.tickshow.backend.request.ResponseToMessageRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class ResponseToMessageUseCase {
    private static final Logger log = LoggerFactory.getLogger(ResponseToMessageUseCase.class);

    private final ContactRepository contactRepository;
    private final ResponseToMessageRequest request;

    public String execute() throws EntityNotFoundException {
        Contact contact = contactRepository.findByIdAndName(request.getId(), request.getName());

        if (contact == null) {
            log.error("Message not found");
            throw new EntityNotFoundException("Message not found");
        }

        contact.setReplied(true);
        contact.setReply(request.getReply());

        contactRepository.save(contact);

        return "Response hans been successfully sent";
    }
}