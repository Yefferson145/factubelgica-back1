package com.factubelgica.api.contexts.billing.controllers;

import com.factubelgica.api.contexts.billing.dtos.CreditNoteCreateRequest;
import com.factubelgica.api.contexts.billing.dtos.CreditNoteResponse;
import com.factubelgica.api.contexts.billing.models.CreditNote;
import com.factubelgica.api.contexts.billing.services.CreditNoteCreate;
import com.factubelgica.api.contexts.billing.services.CreditNoteList;
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
@RequestMapping("a-role/credit-notes")
@RequiredArgsConstructor
public class AdminCreditNoteController {

  private final CreditNoteCreate creditNoteCreate;
  private final CreditNoteList creditNoteList;
  private final CookieUtil cookieUtil;
  private final JwtService jwtService;

  @PostMapping
  public ResponseEntity<CreditNoteResponse> create(
      @RequestBody @Valid CreditNoteCreateRequest req,
      @AuthenticationPrincipal UUID authUserId
  ) {
    CreditNote creditNote = creditNoteCreate.execute(req);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil.createTokenCookie(jwtService.generateToken(authUserId)).toString()
        )
        .body(CreditNoteResponse.fromDomainModel(creditNote));
  }

  @GetMapping
  public ResponseEntity<List<CreditNoteResponse>> list(
      @RequestParam(value = "lastId", required = false) String lastId,
      @RequestParam(value = "limit", defaultValue = "10") int limit,
      @AuthenticationPrincipal UUID authUserId
  ) {
    List<CreditNote> creditNotes = creditNoteList.execute(lastId, limit);
    List<CreditNoteResponse> response = creditNotes.stream()
        .map(CreditNoteResponse::fromDomainModel)
        .toList();

    return ResponseEntity
        .ok()
        .header(
            HttpHeaders.SET_COOKIE,
            cookieUtil.createTokenCookie(jwtService.generateToken(authUserId)).toString()
        )
        .body(response);
  }
}
