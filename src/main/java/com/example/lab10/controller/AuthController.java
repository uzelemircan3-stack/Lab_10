package com.example.lab10.controller;

import com.example.lab10.dto.AuthRequest;
import com.example.lab10.dto.JwtResponse;
import com.example.lab10.dto.RefreshTokenRequest;
import com.example.lab10.entity.RefreshToken;
import com.example.lab10.entity.User;
import com.example.lab10.service.JwtService;
import com.example.lab10.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            if (authentication.isAuthenticated()) {
                UserDetails user = (UserDetails) authentication.getPrincipal();
                String accessToken = jwtService.generateToken(user);


                refreshTokenService.deleteByUserId(user.getUsername());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

                logger.info("User logged in successfully: {}", request.getUsername());

                return ResponseEntity.ok(JwtResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken.getToken())
                        .build());
            } else {
                logger.warn("Failed login attempt for user: {}", request.getUsername());
                throw new UsernameNotFoundException("invalid user request !");
            }
        } catch (BadCredentialsException e) {
            logger.warn("Failed login attempt (Bad Credentials): {}", request.getUsername());
            throw e;
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    User user = refreshToken.getUser();
                    if (user == null) {
                        throw new RuntimeException("Token has no associated user");
                    }
                    String accessToken = jwtService.generateToken(user);

                    refreshTokenService.deleteByUserId(user.getUsername());
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getUsername());

                    logger.info("Token refreshed and rotated for user: {}", user.getUsername());

                    return ResponseEntity.ok(JwtResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(newRefreshToken.getToken())
                            .build());
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {


        String requestRefreshToken = request.get("refreshToken");

        if (requestRefreshToken == null || requestRefreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("refreshToken is required!");
        }

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(token -> {
                    String username = token.getUser().getUsername();
                    refreshTokenService.deleteByUserId(username); // Token sahibinin oturumunu sil
                    logger.info("User logged out via refresh token: {}", username);
                    return ResponseEntity.ok("Logged out successfully");
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}