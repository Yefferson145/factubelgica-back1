package com.factubelgica.api.contexts.user_management.controllers;

import com.factubelgica.api.contexts.user_management.dtos.UserItemResponse;
import com.factubelgica.api.contexts.user_management.dtos.UserRegisterRequest;
import com.factubelgica.api.contexts.user_management.dtos.UserRegisterResponse;
import com.factubelgica.api.contexts.user_management.models.User;
import com.factubelgica.api.contexts.user_management.services.ListUsers;
import com.factubelgica.api.contexts.user_management.services.UserRegister;
import com.factubelgica.api.shared.core.JwtService;
import com.factubelgica.api.shared.utils.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("a-role")
@RequiredArgsConstructor
public class UsersController {
  private final UserRegister register;
  private final ListUsers listUsers;
  private final CookieUtil cookieUtil;
  private final JwtService jwtService;

  @PostMapping("users")
  public ResponseEntity<UserRegisterResponse> register(
      @RequestBody @Valid UserRegisterRequest req,
      @AuthenticationPrincipal UUID authUserId
  ) {
    User user = register.execute(req);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil
                .createTokenCookie(jwtService.generateToken(authUserId))
                .toString()
        )
        .body(UserRegisterResponse.fromUser(user));
  }

  /*
  * El id por defecto es para listar usuarios en la primera peticion
  * ese id es como un 0 si fueran ids numericos consecutivos asiq
  * mostrará si o si el principio de los registros de usuario
  * o lo q seria el usuario 1 en adelante
  */
  @GetMapping("users")
  public ResponseEntity<List<UserItemResponse>> listUserPage(
      @RequestParam(defaultValue = "01942000-0000-71a3-a5bc-b271f284e93d") UUID lastId,
      @RequestParam(defaultValue = "10") int limit,
      @AuthenticationPrincipal UUID authUserId
  ) {
    List<UserItemResponse> userslist = listUsers
        .execute(lastId, limit)
        .stream()
        .map(user -> UserItemResponse.fromUser(user))
        .toList();

    return ResponseEntity
        .status(200)
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil
                .createTokenCookie(jwtService.generateToken(authUserId))
                .toString()
        )
        .body(userslist);
  }
}
