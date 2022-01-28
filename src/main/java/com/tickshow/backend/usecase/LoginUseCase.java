package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.exception.UserLoginException;
import com.tickshow.backend.model.entity.User;
import com.tickshow.backend.model.entity.UserAuthentication;
import com.tickshow.backend.repository.AuthRepository;
import com.tickshow.backend.repository.UserRepository;
import com.tickshow.backend.request.LoginRequest;
import com.tickshow.backend.response.AuthenticationResponse;
import com.tickshow.backend.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@AllArgsConstructor
public class LoginUseCase {
    private static final Logger log = LoggerFactory.getLogger(LoginUseCase.class);

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final LoginRequest request;

    public AuthenticationResponse execute(int expiration) throws EntityNotFoundException, UserLoginException {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            log.error("Account not found for email: {}", request.getEmail());
            throw new EntityNotFoundException("Account not found for email " + request.getEmail());
        }

        String email = user.getEmail();
        UserAuthentication userAuthentication = authRepository.findByEmail(email);

        if (userAuthentication == null) {
            log.error("User authentication not found for email: {}", email);
            throw new EntityNotFoundException("Incorrect email or password, please try again");
        }

        int tries = userAuthentication.getLoginAttempts();
        log.info("Login attempts: {}, for user email: {}", tries, email);

        String jwt;

        try {
            if (tries < 5) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, request.getPassword())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                jwt = jwtUtil.generate(authentication);
            } else {
                throw new UserLoginException("Account blocked, cause:: 5 failure login attempts");
            }
        } catch (AuthenticationException e) {
            if (tries < 5) {
                userAuthentication.setLoginAttempts(tries + 1);
                authRepository.save(userAuthentication);
            }
            log.error("Incorrect email or password, login attempts: {}, for email: {}", tries, email);
            throw new UserLoginException("Incorrect email or password, please try again");
        }

        return new AuthenticationResponse(
                user.getUsername(),
                user.getEmail(),
                jwt,
                expiration
        );
    }
}