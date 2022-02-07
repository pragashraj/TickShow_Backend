package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.exception.MismatchException;
import com.tickshow.backend.model.entity.UserAuthentication;
import com.tickshow.backend.repository.AuthRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class ConfirmResetCodeUseCase {
    private static final Logger log = LoggerFactory.getLogger(ConfirmResetCodeUseCase.class);

    private final AuthRepository authRepository;

    public String execute(String email, String code) throws EntityNotFoundException, MismatchException {
        UserAuthentication userAuthentication = authRepository.findByEmail(email);

        if (userAuthentication == null) {
            log.error("Incorrect email address : {}", email);
            throw new EntityNotFoundException("User not found, Incorrect email address");
        }

        String passwordResetKey = userAuthentication.getPasswordResetKey();

        if (passwordResetKey != null) {
            if (!passwordResetKey.equals(code)) {
                throw new MismatchException("Incorrect reset code, please try again");
            }
        } else {
            log.error("Password reset key not found for email address : {}", email);
            throw new EntityNotFoundException("Password reset key not found");
        }

        return "Reset code is confirmed";
    }
}