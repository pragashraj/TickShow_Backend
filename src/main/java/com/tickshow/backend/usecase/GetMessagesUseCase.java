package com.tickshow.backend.usecase;

import com.tickshow.backend.model.coreEntity.CoreContact;
import com.tickshow.backend.model.entity.Contact;
import com.tickshow.backend.model.pageableEntity.PageableCoreContact;
import com.tickshow.backend.repository.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

@AllArgsConstructor
public class GetMessagesUseCase {
    private final ContactRepository contactRepository;

    public PageableCoreContact execute(int page) {
        Page<Contact> contactPage = contactRepository.findAll(PageRequest.of(page, 10));

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