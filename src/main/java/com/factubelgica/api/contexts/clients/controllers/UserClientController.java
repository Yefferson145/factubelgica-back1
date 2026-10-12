package com.factubelgica.api.contexts.clients.controllers;

import com.factubelgica.api.contexts.clients.dtos.ClientCreateRequest;
import com.factubelgica.api.contexts.clients.dtos.ClientResponse;
import com.factubelgica.api.contexts.clients.dtos.ClientUpdateRequest;
import com.factubelgica.api.contexts.clients.models.Client;
import com.factubelgica.api.contexts.clients.services.ClientCreate;
import com.factubelgica.api.contexts.clients.services.ClientGet;
import com.factubelgica.api.contexts.clients.services.ClientList;
import com.factubelgica.api.contexts.clients.services.ClientUpdate;
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
@RequestMapping("u-role/clients")
@RequiredArgsConstructor
public class UserClientController {

  private final ClientCreate clientCreate;
  private final ClientUpdate clientUpdate;
  private final ClientList clientList;
  private final ClientGet clientGet;
  private final CookieUtil cookieUtil;
  private final JwtService jwtService;

  @PostMapping
  public ResponseEntity<ClientResponse> create(
      @RequestBody @Valid ClientCreateRequest req,
      @AuthenticationPrincipal UUID authUserId
  ) {
    Client client = clientCreate.execute(req);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil
                .createTokenCookie(jwtService.generateToken(authUserId))
                .toString()
        )
        .body(ClientResponse.fromClient(client));
  }

  @PutMapping("{id}")
  public ResponseEntity<ClientResponse> update(
      @PathVariable Integer id,
      @RequestBody @Valid ClientUpdateRequest req,
      @AuthenticationPrincipal UUID authUserId
  ) {
    Client client = clientUpdate.execute(id, req);
    return ResponseEntity
        .status(HttpStatus.OK)
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil
                .createTokenCookie(jwtService.generateToken(authUserId))
                .toString()
        )
        .body(ClientResponse.fromClient(client));
  }

  @GetMapping
  public ResponseEntity<List<ClientResponse>> list(
      @RequestParam(defaultValue = "0") Integer lastId,
      @RequestParam(defaultValue = "10") int limit,
      @AuthenticationPrincipal UUID authUserId
  ) {
    List<ClientResponse> response = clientList.execute(lastId, limit)
        .stream()
        .map(ClientResponse::fromClient)
        .toList();
    return ResponseEntity
        .status(HttpStatus.OK)
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil
                .createTokenCookie(jwtService.generateToken(authUserId))
                .toString()
        )
        .body(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<ClientResponse> get(
      @PathVariable Integer id,
      @AuthenticationPrincipal UUID authUserId
  ) {
    Client client = clientGet.execute(id);
    return ResponseEntity
        .status(HttpStatus.OK)
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil
                .createTokenCookie(jwtService.generateToken(authUserId))
                .toString()
        )
        .body(ClientResponse.fromClient(client));
  }
}
