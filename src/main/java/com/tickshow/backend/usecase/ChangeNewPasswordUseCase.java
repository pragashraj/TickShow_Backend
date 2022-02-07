package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.exception.MismatchException;
import com.tickshow.backend.model.entity.UserAuthentication;
import com.tickshow.backend.repository.AuthRepository;
import com.tickshow.backend.request.ChangeNewPasswordRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class ChangeNewPasswordUseCase {
    private static final Logger log = LoggerFactory.getLogger(ChangeNewPasswordUseCase.class);

    private final AuthRepository authRepository;
    private final ChangeNewPasswordRequest request;
    private final PasswordEncoder passwordEncoder;

    public String execute() throws EntityNotFoundException, MismatchException {
        UserAuthentication userAuthentication = authRepository.findByEmail(request.getEmail());

        if (userAuthentication == null) {
            log.error("Incorrect email address : {}", request.getEmail());
            throw new EntityNotFoundException("User not found, Incorrect email address");
        }

        if (passwordEncoder.matches(request.getOldPassword(), userAuthentication.getPassword())) {
            userAuthentication.setPassword(passwordEncoder.encode(request.getNewPassword()));
            authRepository.save(userAuthentication);
        } else {
            log.error("Incorrect old Password");
            throw new MismatchException("Incorrect old Password");
        }

        return "Password changed successfully";
    }
}