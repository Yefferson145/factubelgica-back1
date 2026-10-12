package com.factubelgica.api.contexts.catalog.controllers;

import com.factubelgica.api.contexts.catalog.dtos.ProductItemResponse;
import com.factubelgica.api.contexts.catalog.services.ProductList;
import com.factubelgica.api.shared.core.JwtService;
import com.factubelgica.api.shared.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("u-role")
@RequiredArgsConstructor
public class UserProductController {

  private final ProductList productList;
  private final CookieUtil cookieUtil;
  private final JwtService jwtService;

  @GetMapping("products")
  public ResponseEntity<List<ProductItemResponse>> listProductsPage(
      @RequestParam(defaultValue = "0") Integer lastId,
      @RequestParam(defaultValue = "10") int limit,
      @AuthenticationPrincipal UUID authUserId
  ) {
    List<ProductItemResponse> response = productList
        .execute(lastId, limit)
        .stream()
        .map(ProductItemResponse::fromProduct)
        .toList();

    return ResponseEntity
        .status(200)
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil
                .createTokenCookie(jwtService.generateToken(authUserId))
                .toString()
        )
        .body(response);
  }
}