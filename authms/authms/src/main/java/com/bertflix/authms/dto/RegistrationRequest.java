package com.bertflix.authms.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String name;
    private String username;
    private String email;
    private String password;
}
