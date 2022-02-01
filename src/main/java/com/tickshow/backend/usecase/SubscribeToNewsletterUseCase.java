package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.AlreadyExistException;
import com.tickshow.backend.model.entity.Subscriber;
import com.tickshow.backend.repository.SubscriberRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class SubscribeToNewsletterUseCase {
    private static final Logger log = LoggerFactory.getLogger(SubscribeToNewsletterUseCase.class);

    private final SubscriberRepository subscriberRepository;

    public String execute(String email) throws AlreadyExistException {
        Subscriber existence = subscriberRepository.findByEmail(email);

        if (existence != null) {
            log.error("Subscriber already exist for email: {}", email);
            throw new AlreadyExistException("You already subscribed with this email");
        }

        Subscriber subscriber = Subscriber.builder()
                .email(email)
                .build();

        subscriberRepository.save(subscriber);

        return "Subscribed to newsletter successfully";
    }
}