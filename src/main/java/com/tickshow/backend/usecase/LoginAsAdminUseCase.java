package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.exception.UserLoginException;
import com.tickshow.backend.model.entity.Admin;
import com.tickshow.backend.model.entity.UserAuthentication;
import com.tickshow.backend.repository.AdminRepository;
import com.tickshow.backend.repository.AuthRepository;
import com.tickshow.backend.request.LoginAsAdminRequest;
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
public class LoginAsAdminUseCase {
    private static final Logger log = LoggerFactory.getLogger(LoginAsAdminUseCase.class);

    private final AuthRepository authRepository;
    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final LoginAsAdminRequest request;

    public AuthenticationResponse execute(int expiration) throws EntityNotFoundException, UserLoginException {
        String email = request.getEmail();

        Admin admin = adminRepository.findByIdAndEmail(request.getId(), email);

        if (admin == null) {
            log.error("Account not found for email: {} and id :{}", email, request.getId());
            throw new EntityNotFoundException("Account not found for email " + email);
        }

        UserAuthentication userAuthentication = authRepository.findByAdmin(admin);

        if (userAuthentication == null) {
            log.error("Admin authentication not found for email: {}", email);
            throw new EntityNotFoundException("Incorrect email or password, please try again");
        }

        int tries = userAuthentication.getLoginAttempts();
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
                admin.getUsername(),
                admin.getEmail(),
                jwt,
                expiration
        );
    }
}