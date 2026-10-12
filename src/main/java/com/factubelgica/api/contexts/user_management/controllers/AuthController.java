package com.factubelgica.api.contexts.user_management.controllers;

import com.factubelgica.api.contexts.user_management.dtos.UserLoginRequest;
import com.factubelgica.api.contexts.user_management.dtos.UserLoginResponse;
import com.factubelgica.api.contexts.user_management.models.User;
import com.factubelgica.api.contexts.user_management.services.UserLogin;
import com.factubelgica.api.shared.core.JwtService;
import com.factubelgica.api.shared.utils.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserLogin login;
  private final CookieUtil cookieUtil;
  private final JwtService jwtService;

  @PostMapping("login")
  public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest req) {
    User user = login.execute(req);

    return ResponseEntity
        .status(200)
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil
                .createTokenCookie(jwtService.generateToken(user.getUserId()))
                .toString()
        )
        .body(UserLoginResponse.fromUser(user));
  }
}
