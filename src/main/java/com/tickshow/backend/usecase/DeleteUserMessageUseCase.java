package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.coreEntity.CoreContact;
import com.tickshow.backend.model.entity.Contact;
import com.tickshow.backend.model.pageableEntity.PageableCoreContact;
import com.tickshow.backend.repository.ContactRepository;
import com.tickshow.backend.request.DeleteUserMessagesRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DeleteUserMessageUseCase {
    private static final Logger log = LoggerFactory.getLogger(DeleteUserMessageUseCase.class);

    private final ContactRepository contactRepository;
    private final DeleteUserMessagesRequest request;

    public PageableCoreContact execute() throws EntityNotFoundException {
        for (Long id : request.getIds()) {

            Optional<Contact> contactOptional = contactRepository.findById(id);

            if (!contactOptional.isPresent()) {
                log.error("Message not found");
                throw new EntityNotFoundException("Message not found");
            }

            Contact contact = contactOptional.get();

            contactRepository.delete(contact);
        }

        Page<Contact> contactPage = contactRepository.findAll(PageRequest.of(0, 10));

        return new PageableCoreContact(
                contactPage.get().map(this::convertToCoreEntity).collect(Collectors.toList()),
                contactPage.getTotalPages(),
                contactPage.getNumber()
        );
    }

    private CoreContact convertToCoreEntity(Contact contact) {
        return new CoreContact(
                contact.getId(),
                contact.getName(),
                contact.getEmail(),
                contact.getSubject(),
                contact.getDateTime(),
                contact.getMessage(),
                contact.isReplied()
        );
    }
}