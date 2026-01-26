package com.example.lab10.service;

import com.example.lab10.entity.RefreshToken;
import com.example.lab10.entity.User;
import com.example.lab10.repository.RefreshTokenRepository;
import com.example.lab10.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void createRefreshToken_Success() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

        RefreshToken result = refreshTokenService.createRefreshToken("testUser");

        assertNotNull(result);
        assertEquals(user, result.getUser());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void findByToken_shouldReturnToken() {
        String tokenStr = "uuid";
        RefreshToken token = new RefreshToken();
        token.setToken(tokenStr);
        when(refreshTokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(token));

        Optional<RefreshToken> result = refreshTokenService.findByToken(tokenStr);

        assertTrue(result.isPresent());
        assertEquals(tokenStr, result.get().getToken());
    }

    @Test
    void verifyExpiration_whenNotExpired_shouldReturnToken() {
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().plusSeconds(60));

        RefreshToken result = refreshTokenService.verifyExpiration(token);

        assertEquals(token, result);
    }

    @Test
    void verifyExpiration_whenExpired_shouldThrowException() {
        RefreshToken token = new RefreshToken();
        token.setToken("expired");
        token.setExpiryDate(Instant.now().minusSeconds(60));

        assertThrows(RuntimeException.class, () -> refreshTokenService.verifyExpiration(token));
        verify(refreshTokenRepository).delete(token);
    }

    @Test
    void deleteByUserId_shouldCallRepository() {
        String username = "testUser";
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        refreshTokenService.deleteByUserId(username);

        verify(refreshTokenRepository).deleteByUser(user);
    }
}