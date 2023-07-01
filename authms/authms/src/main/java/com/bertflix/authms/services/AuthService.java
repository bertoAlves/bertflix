package com.bertflix.authms.services;

import com.bertflix.authms.dto.LoginRequest;
import com.bertflix.authms.dto.LoginResponse;
import com.bertflix.authms.model.ConfirmationToken;
import com.bertflix.authms.repositories.ConfirmationTokenRepository;
import com.bertflix.authms.security.config.ApplicationConfig;
import com.bertflix.authms.dto.RegistrationRequest;
import com.bertflix.authms.model.AppUserRole;
import com.bertflix.authms.model.User;
import com.bertflix.authms.repositories.UserRepository;
import com.bertflix.authms.security.config.JwtService;
import com.bertflix.authms.tools.EmailValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "User with username %s not found";
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final AuthenticationManager authenticationManager;
    private final ApplicationConfig applicationConfig;

    private final JwtService jwtService;

    private final EmailValidator emailValidator;
    private final EmailSender emailSender;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername().toLowerCase(), request.getPassword()));

        User user = userRepository.findByUsernameOrEmail(request.getUsername().toLowerCase(), request.getUsername().toLowerCase()).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, request.getUsername())));

        return new LoginResponse(jwtService.generateToken(user));
    }

    public String register(RegistrationRequest request) {
        boolean isValid = emailValidator.test(request.getEmail());
        if (!isValid) {
            throw new IllegalStateException("Email not valid");
        }

        User user = new User(request.getName(), request.getUsername().toLowerCase(), request.getEmail().toLowerCase(), applicationConfig.passwordEncoder().encode(request.getPassword()), AppUserRole.ADMIN, false, false);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        this.saveConfirmationToken(
                confirmationToken);

        emailSender.sendConfirmationEmail(request.getEmail(),request.getName(),token);
        return "OK";
    }

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsernameOrEmail(username.toLowerCase(), username.toLowerCase()).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }
}
