package com.factubelgica.api.contexts.catalog.controllers;

import com.factubelgica.api.contexts.catalog.dtos.ProductCreateRequest;
import com.factubelgica.api.contexts.catalog.dtos.ProductCreateResponse;
import com.factubelgica.api.contexts.catalog.models.Product;
import com.factubelgica.api.contexts.catalog.services.ProductCreate;
import com.factubelgica.api.shared.core.JwtService;
import com.factubelgica.api.shared.utils.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("a-role")
@RequiredArgsConstructor
public class AdminProductController {

  private final ProductCreate productCreate;
  private final CookieUtil cookieUtil;
  private final JwtService jwtService;

  @PostMapping("products")
  public ResponseEntity<ProductCreateResponse> create(
      @RequestBody @Valid ProductCreateRequest req,
      @AuthenticationPrincipal UUID authUserId
  ) {
    Product product = productCreate.execute(req);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil
                .createTokenCookie(jwtService.generateToken(authUserId))
                .toString()
        )
        .body(ProductCreateResponse.fromProduct(product));
  }
}