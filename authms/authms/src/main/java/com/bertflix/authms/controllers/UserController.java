package com.bertflix.authms.controllers;

import com.bertflix.authms.services.UserService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import com.bertflix.authms.dto.UserRequest.*;

@RestController
@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/change/password")
    public String changePassword(HttpServletRequest headers, @RequestBody ChangePasswordRequest request) {
        return userService.changePassword(headers, request);
    }

    @PutMapping("/change/name")
    public String changeName(HttpServletRequest headers, @RequestBody ChangeNameRequest request) {
        return userService.changeName(headers, request);
    }

    @PutMapping("/change/email")
    public String changeEmail(HttpServletRequest headers, @RequestBody ChangeEmailRequest request) {
        return userService.changeEmail(headers, request);
    }
}
