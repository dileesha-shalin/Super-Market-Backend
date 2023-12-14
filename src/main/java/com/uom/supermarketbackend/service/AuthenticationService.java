package com.uom.supermarketbackend.service;

import com.uom.supermarketbackend.dto.AuthenticationRequestDTO;
import com.uom.supermarketbackend.dto.AuthenticationResponseDTO;
import com.uom.supermarketbackend.dto.RegistrationRequestDTO;
import com.uom.supermarketbackend.enums.RoleType;
import com.uom.supermarketbackend.enums.TokenType;
import com.uom.supermarketbackend.model.Token;
import com.uom.supermarketbackend.model.User;
import com.uom.supermarketbackend.repository.TokenRepository;
import com.uom.supermarketbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO register(RegistrationRequestDTO request) {
        try {
            var role = request.getRole();

            var currentUser = userRepository.findByEmailAndRole(request.getEmail(), role);

            if (currentUser.isPresent()) {
                throw new RuntimeException("User already exists");
            }

            var user = User.builder()
                    .firstname(request.getFirstName())
                    .lastname(request.getLastName())
                    .email(request.getEmail())
                    .role(RoleType.valueOf(request.getRole().name()))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            var savedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);

            saveUserToken(savedUser, jwtToken);
            return AuthenticationResponseDTO.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        }

     catch (Exception e){

         System.err.println("Error during registration: " + e.getMessage());
         throw e; // Ensure the exception is propagated
     }

    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Boolean validateToken(String token) {
        var user = tokenRepository.findUserByToken(token);

        if(user.isEmpty()) return false;

        System.out.println("token " + token);

        return jwtService.isTokenValid(token, user.get());
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

}
