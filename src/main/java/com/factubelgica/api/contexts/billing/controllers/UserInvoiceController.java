package com.factubelgica.api.contexts.billing.controllers;

import com.factubelgica.api.contexts.billing.dtos.InvoiceCreateRequest;
import com.factubelgica.api.contexts.billing.dtos.InvoiceCreateResponse;
import com.factubelgica.api.contexts.billing.dtos.InvoiceStatusUpdateRequest;
import com.factubelgica.api.contexts.billing.models.Invoice;
import com.factubelgica.api.contexts.billing.services.InvoiceCreate;
import com.factubelgica.api.contexts.billing.services.InvoiceGet;
import com.factubelgica.api.contexts.billing.services.InvoiceList;
import com.factubelgica.api.contexts.billing.services.InvoiceStatusUpdate;
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
@RequestMapping("u-role/invoices")
@RequiredArgsConstructor
public class UserInvoiceController {

  private final InvoiceCreate invoiceCreate;
  private final InvoiceGet invoiceGet;
  private final InvoiceList invoiceList;
  private final InvoiceStatusUpdate invoiceStatusUpdate;
  private final CookieUtil cookieUtil;
  private final JwtService jwtService;

  @PostMapping
  public ResponseEntity<InvoiceCreateResponse> create(
      @RequestBody @Valid InvoiceCreateRequest req,
      @AuthenticationPrincipal UUID authUserId
  ) {
    Invoice invoice = invoiceCreate.execute(req, authUserId);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil.createTokenCookie(jwtService.generateToken(authUserId)).toString()
        )
        .body(InvoiceCreateResponse.fromDomainModel(invoice));
  }

  @GetMapping("{id}")
  public ResponseEntity<InvoiceCreateResponse> get(
      @PathVariable("id") UUID id,
      @AuthenticationPrincipal UUID authUserId
  ) {
    Invoice invoice = invoiceGet.execute(id, authUserId);
    return ResponseEntity
        .ok()
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil.createTokenCookie(jwtService.generateToken(authUserId)).toString()
        )
        .body(InvoiceCreateResponse.fromDomainModel(invoice));
  }

  @GetMapping
  public ResponseEntity<List<InvoiceCreateResponse>> list(
      @RequestParam(value = "lastId", required = false) String lastId,
      @RequestParam(value = "limit", defaultValue = "10") int limit,
      @AuthenticationPrincipal UUID authUserId
  ) {
    List<Invoice> invoices = invoiceList.execute(lastId, limit, authUserId);
    List<InvoiceCreateResponse> response = invoices.stream()
        .map(InvoiceCreateResponse::fromDomainModel)
        .toList();

    return ResponseEntity
        .ok()
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil.createTokenCookie(jwtService.generateToken(authUserId)).toString()
        )
        .body(response);
  }

  @PutMapping("{id}/status")
  public ResponseEntity<InvoiceCreateResponse> updateStatus(
      @PathVariable("id") UUID id,
      @RequestBody @Valid InvoiceStatusUpdateRequest req,
      @AuthenticationPrincipal UUID authUserId
  ) {
    Invoice invoice = invoiceStatusUpdate.execute(id, req, authUserId);
    return ResponseEntity
        .ok()
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil.createTokenCookie(jwtService.generateToken(authUserId)).toString()
        )
        .body(InvoiceCreateResponse.fromDomainModel(invoice));
  }
}
