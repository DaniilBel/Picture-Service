package org.project.pictureservice.service;

import org.project.pictureservice.web.dto.auth.JwtRequest;
import org.project.pictureservice.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(
            JwtRequest loginRequest
    );

    JwtResponse refresh(
            String refreshToken
    );
}
