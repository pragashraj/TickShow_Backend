package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.ContentCreationException;
import com.tickshow.backend.exception.DispatcherException;
import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.entity.User;
import com.tickshow.backend.model.entity.UserAuthentication;
import com.tickshow.backend.repository.AuthRepository;
import com.tickshow.backend.transport.EmailService;
import com.tickshow.backend.transport.templates.PasswordResetTemplate;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.RandomStringUtils;

@AllArgsConstructor
public class SendPasswordResetCodeUseCase {
    private static final Logger log = LoggerFactory.getLogger(SendPasswordResetCodeUseCase.class);

    private final AuthRepository authRepository;
    private final PasswordResetTemplate passwordResetTemplate;
    private final EmailService emailService;

    public String execute(String email) throws EntityNotFoundException, DispatcherException, ContentCreationException {
        UserAuthentication userAuthentication = authRepository.findByEmail(email);

        if (userAuthentication == null) {
            log.error("Incorrect email address : {}", email);
            throw new EntityNotFoundException("User not found, Incorrect email address");
        }

        String passwordResetKey = RandomStringUtils.randomAlphanumeric(6);

        dispatchEmail(userAuthentication.getUser(), email, passwordResetKey);

        userAuthentication.setPasswordResetKey(passwordResetKey);
        authRepository.save(userAuthentication);

        log.info("Password reset code has been sent to email : {} & reset-code: {}", email, passwordResetKey);

        return "Reset code has been sent, please check your email";
    }

    private void dispatchEmail(User user, String email, String key) throws ContentCreationException, DispatcherException {
        String subject = "Password reset Code";
        String content = passwordResetTemplate.getContent(
                user.getUsername(),
                key
        );
        emailService.sendEmail(email, subject, content);
    }
}