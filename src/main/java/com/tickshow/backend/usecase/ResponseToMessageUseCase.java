package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.ContentCreationException;
import com.tickshow.backend.exception.DispatcherException;
import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.entity.Contact;
import com.tickshow.backend.repository.ContactRepository;
import com.tickshow.backend.request.ResponseToMessageRequest;
import com.tickshow.backend.transport.EmailService;
import com.tickshow.backend.transport.templates.MessageResponseTemplate;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class ResponseToMessageUseCase {
    private static final Logger log = LoggerFactory.getLogger(ResponseToMessageUseCase.class);

    private final ContactRepository contactRepository;
    private final ResponseToMessageRequest request;
    private final MessageResponseTemplate messageResponseTemplate;
    private final EmailService emailService;

    public String execute() throws EntityNotFoundException, DispatcherException, ContentCreationException {
        Contact contact = contactRepository.findByIdAndName(request.getId(), request.getName());

        if (contact == null) {
            log.error("Message not found");
            throw new EntityNotFoundException("Message not found");
        }

        contact.setReplied(true);
        contact.setReply(request.getReply());

        contactRepository.save(contact);

        dispatchEmail(request.getName(), contact.getEmail(), request.getReply());

        return "Response hans been successfully sent";
    }

    private void dispatchEmail(String name, String email, String reply) throws ContentCreationException, DispatcherException {
        String subject = "Password reset Code";
        String content = messageResponseTemplate.getContent(name, reply);
        emailService.sendEmail(email, subject, content);
    }
}