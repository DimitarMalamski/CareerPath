package com.careerpath.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthSyncResponse {
    private String token;
}