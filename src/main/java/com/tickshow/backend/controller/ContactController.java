package com.tickshow.backend.controller;

import com.tickshow.backend.exception.ContentCreationException;
import com.tickshow.backend.exception.DispatcherException;
import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.pageableEntity.PageableCoreContact;
import com.tickshow.backend.repository.ContactRepository;
import com.tickshow.backend.request.ResponseToMessageRequest;
import com.tickshow.backend.request.SendMessageRequest;
import com.tickshow.backend.response.ApiResponse;
import com.tickshow.backend.transport.EmailService;
import com.tickshow.backend.transport.templates.MessageResponseTemplate;
import com.tickshow.backend.usecase.GetMessagesByIsRepliedUseCase;
import com.tickshow.backend.usecase.GetMessagesUseCase;
import com.tickshow.backend.usecase.ResponseToMessageUseCase;
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
    private final MessageResponseTemplate messageResponseTemplate;
    private final EmailService emailService;

    @Autowired
    public ContactController(ContactRepository contactRepository,
                             MessageResponseTemplate messageResponseTemplate,
                             EmailService emailService
    ) {
        this.contactRepository = contactRepository;
        this.messageResponseTemplate = messageResponseTemplate;
        this.emailService = emailService;
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

    @GetMapping("get-messages/{page}")
    public ResponseEntity<?> getMessages(@PathVariable int page) {
        try {
            GetMessagesUseCase useCase = new GetMessagesUseCase(contactRepository);
            PageableCoreContact pageableCoreContact = useCase.execute(page);
            return ResponseEntity.ok(pageableCoreContact);
        } catch (Exception e) {
            log.error("Unable to get user messages, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("get-messages-by-isReplied")
    public ResponseEntity<?> getMessagesByIsReplied(@RequestParam boolean isReplied, @RequestParam int page) {
        try {
            GetMessagesByIsRepliedUseCase useCase = new GetMessagesByIsRepliedUseCase(contactRepository);
            PageableCoreContact pageableCoreContact = useCase.execute(isReplied, page);
            return ResponseEntity.ok(pageableCoreContact);
        } catch (Exception e) {
            log.error("Unable to get user messages by isReplied, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("response-to-user-message")
    public ResponseEntity<?> responseToMessage(@RequestBody ResponseToMessageRequest request) {
        try {
            ResponseToMessageUseCase useCase = new ResponseToMessageUseCase(
                    contactRepository,
                    request,
                    messageResponseTemplate,
                    emailService
            );
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            log.error("Unable to sent response to user messages, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception | DispatcherException | ContentCreationException e) {
            log.error("Unable to sent response to user messages, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}