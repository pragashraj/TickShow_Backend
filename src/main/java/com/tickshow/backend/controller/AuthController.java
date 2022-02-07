package com.tickshow.backend.controller;

import com.tickshow.backend.exception.*;
import com.tickshow.backend.repository.AuthRepository;
import com.tickshow.backend.repository.UserRepository;
import com.tickshow.backend.request.LoginRequest;
import com.tickshow.backend.request.SignUpRequest;
import com.tickshow.backend.response.ApiResponse;
import com.tickshow.backend.response.AuthenticationResponse;
import com.tickshow.backend.transport.EmailService;
import com.tickshow.backend.transport.templates.PasswordResetTemplate;
import com.tickshow.backend.usecase.LoginUseCase;
import com.tickshow.backend.usecase.RegisterUseCase;
import com.tickshow.backend.usecase.SendPasswordResetCodeUseCase;
import com.tickshow.backend.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth/")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final PasswordResetTemplate passwordResetTemplate;
    private final EmailService emailService;

    @Value("${app.jwt.expiration}")
    private int expiration;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil,
                          AuthRepository authRepository,
                          UserRepository userRepository,
                          PasswordResetTemplate passwordResetTemplate,
                          EmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.passwordResetTemplate = passwordResetTemplate;
        this.emailService = emailService;
    }

    @PostMapping("sign-up")
    public ResponseEntity<?> register(@RequestBody SignUpRequest request) {
        try {
            RegisterUseCase useCase = new RegisterUseCase(
                    authRepository,
                    userRepository,
                    passwordEncoder,
                    request
            );
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (RegisterException e) {
            log.error("Unable to register user, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to register user, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginUseCase useCase = new LoginUseCase(
                    authRepository,
                    userRepository,
                    jwtUtil,
                    authenticationManager,
                    request
            );
            AuthenticationResponse response = useCase.execute(expiration);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException | UserLoginException e) {
            log.error("Unable to login, incorrect email or password, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to login, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("send-reset-code/{email}")
    public ResponseEntity<?> sendResetCode(@PathVariable String email) {
        try {
            SendPasswordResetCodeUseCase useCase = new SendPasswordResetCodeUseCase(
                    authRepository,
                    passwordResetTemplate,
                    emailService
            );
            String response = useCase.execute(email);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException | DispatcherException | ContentCreationException e) {
            log.error("Unable to send reset code, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to send reset code, cause : {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}