package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.RegisterException;
import com.tickshow.backend.model.entity.User;
import com.tickshow.backend.model.entity.UserAuthentication;
import com.tickshow.backend.repository.AuthRepository;
import com.tickshow.backend.repository.UserRepository;
import com.tickshow.backend.request.SignUpRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class RegisterUseCase {
    private static final Logger log = LoggerFactory.getLogger(RegisterUseCase.class);

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignUpRequest request;

    public String execute() throws RegisterException {
        UserAuthentication userAuthentication = authRepository.findByEmail(request.getEmail());

        if (userAuthentication != null) {
            log.error("Account Already exist with same email: {}", request.getEmail());
            throw new RegisterException("Account already exist with same email");
        }

        User user = User
                .builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .build();
        userRepository.save(user);

        UserAuthentication userAuth = UserAuthentication
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .loginAttempts(0)
                .user(user)
                .build();
        authRepository.save(userAuth);

        return "Registered successfully";
    }
}