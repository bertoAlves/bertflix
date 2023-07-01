package com.bertflix.authms.controllers;

import com.bertflix.authms.dto.*;
import com.bertflix.authms.services.ConfirmationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.bertflix.authms.services.AuthService;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ConfirmationTokenService confirmationTokenService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegistrationRequest request) {
        return authService.register(request);
    }

    @GetMapping(path = "register/confirm")
    public String confirm(@RequestParam("token") String token) {
        return confirmationTokenService.confirmToken(token);
    }

    @GetMapping("/validate")
    public void validate() {

    }
}
