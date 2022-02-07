package com.tickshow.backend.controller;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.exception.MismatchException;
import com.tickshow.backend.repository.AuthRepository;
import com.tickshow.backend.request.ChangeNewPasswordRequest;
import com.tickshow.backend.response.ApiResponse;
import com.tickshow.backend.usecase.ChangeNewPasswordUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user/")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(AuthRepository authRepository,
                          PasswordEncoder passwordEncoder
    ) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangeNewPasswordRequest request) {
        try {
            ChangeNewPasswordUseCase useCase = new ChangeNewPasswordUseCase(authRepository, request, passwordEncoder);
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException | MismatchException e) {
            log.error("Unable to change new password, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to change new password, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}