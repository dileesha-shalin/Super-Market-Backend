package com.uom.supermarketbackend.controller;

import com.uom.supermarketbackend.dto.AuthenticationRequestDTO;
import com.uom.supermarketbackend.dto.AuthenticationResponseDTO;
import com.uom.supermarketbackend.dto.RegistrationRequestDTO;
import com.uom.supermarketbackend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(
            @RequestBody RegistrationRequestDTO request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(
            @RequestParam String token
    ) {
        return ResponseEntity.ok(service.validateToken(token));
    }
}