package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.entity.UserAuthentication;
import com.tickshow.backend.repository.AuthRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class ForgotPasswordUseCase {
    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordUseCase.class);

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public String execute(String email, String password) throws EntityNotFoundException {
        UserAuthentication userAuthentication = authRepository.findByEmail(email);

        if (userAuthentication == null) {
            log.error("Incorrect email address : {}", email);
            throw new EntityNotFoundException("User not found, Incorrect email address");
        }

        userAuthentication.setPassword(passwordEncoder.encode(password));
        userAuthentication.setPasswordResetKey(null);
        authRepository.save(userAuthentication);

        return "Password changed successfully";
    }
}