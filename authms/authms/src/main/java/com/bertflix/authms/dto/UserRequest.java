package com.bertflix.authms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class UserRequest {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangeEmailRequest {
        private String new_email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangeNameRequest {
        private String new_name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangePasswordRequest {
        private String new_password;
    }
}