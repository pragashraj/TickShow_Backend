package com.tickshow.backend.controller;

import com.tickshow.backend.exception.*;
import com.tickshow.backend.repository.AdminRepository;
import com.tickshow.backend.repository.AuthRepository;
import com.tickshow.backend.repository.UserRepository;
import com.tickshow.backend.request.LoginAsAdminRequest;
import com.tickshow.backend.request.LoginRequest;
import com.tickshow.backend.request.SignUpRequest;
import com.tickshow.backend.response.ApiResponse;
import com.tickshow.backend.response.AuthenticationResponse;
import com.tickshow.backend.transport.EmailService;
import com.tickshow.backend.transport.templates.PasswordResetTemplate;
import com.tickshow.backend.usecase.*;
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

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordResetTemplate passwordResetTemplate;
    private final EmailService emailService;

    @Value("${app.jwt.expiration}")
    private int expiration;

    @Autowired
    public AuthController(AuthRepository authRepository,
                          UserRepository userRepository,
                          AdminRepository adminRepository,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil,
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
        this.adminRepository = adminRepository;
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

    @PostMapping("confirm-reset-code")
    public ResponseEntity<?> confirmResetCode(@RequestParam String email, @RequestParam String code) {
        try {
            ConfirmResetCodeUseCase useCase = new ConfirmResetCodeUseCase(authRepository);
            String response = useCase.execute(email, code);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException | MismatchException e) {
            log.error("Unable to confirm reset code, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to confirm reset code, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestParam String email, @RequestParam String newPassword) {
        try {
            ForgotPasswordUseCase useCase = new ForgotPasswordUseCase(
                    authRepository,
                    passwordEncoder
            );
            String response = useCase.execute(email, newPassword);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            log.error("Unable to change new password, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to change new password, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("admin-sign-in")
    public ResponseEntity<?> loginAsAdmin(@RequestBody LoginAsAdminRequest request) {
        try {
            LoginAsAdminUseCase useCase = new LoginAsAdminUseCase(
                    authRepository,
                    adminRepository,
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
}