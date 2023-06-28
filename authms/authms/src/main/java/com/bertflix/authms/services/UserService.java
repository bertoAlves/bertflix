package com.bertflix.authms.services;

import com.bertflix.authms.model.ConfirmationToken;
import com.bertflix.authms.model.User;
import com.bertflix.authms.repositories.UserRepository;
import com.bertflix.authms.security.config.ApplicationConfig;
import com.bertflix.authms.security.config.JwtService;
import com.bertflix.authms.tools.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import com.bertflix.authms.dto.UserRequest.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final static String USER_NOT_FOUND = "User with username %s not found";
    private final JwtService jwtService;
    private final AuthService authService;

    private final UserRepository userRepository;
    private final ApplicationConfig applicationConfig;

    private final EmailValidator emailValidator;
    private final EmailSender emailSender;

    public String changeName(HttpServletRequest headers, ChangeNameRequest request) {
        final String authHeader = headers.getHeader("Authorization");
        final String jwt;
        final String email;
        jwt = authHeader.substring(7);
        email = jwtService.extractEmail(jwt);
        User user = userRepository.findByUsernameOrEmail(email, email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
        user.setName(request.getNew_name());
        userRepository.save(user);
        return "OK";
    }

    public String changeEmail(HttpServletRequest headers, ChangeEmailRequest request) {
        boolean isValid = emailValidator.test(request.getNew_email());
        if (!isValid) {
            throw new IllegalStateException("Email not valid");
        }

        final String authHeader = headers.getHeader("Authorization");
        final String jwt;
        final String email;
        jwt = authHeader.substring(7);
        email = jwtService.extractEmail(jwt);

        User user = userRepository.findByUsernameOrEmail(email, email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
        user.setEmail(request.getNew_email());
        user.setActive(false);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        authService.saveConfirmationToken(
                confirmationToken);

        emailSender.sendConfirmationEmail(user.getEmail(),user.getName(),token);
        return "OK";
    }

    public String changePassword(HttpServletRequest headers, ChangePasswordRequest request) {
        final String authHeader = headers.getHeader("Authorization");
        final String jwt;
        final String email;
        jwt = authHeader.substring(7);
        email = jwtService.extractEmail(jwt);
        User user = userRepository.findByUsernameOrEmail(email, email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
        user.setPassword(applicationConfig.passwordEncoder().encode(request.getNew_password()));
        userRepository.save(user);
        return "OK";
    }
}
