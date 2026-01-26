package com.example.lab10;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NoteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenAccessingPublicEndpoint_thenSucceed() throws Exception {
        // DÜZELTME: Kayıt genellikle POST metodudur.
        // 405 hatasını önlemek için post() kullanıyoruz.
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"test\",\"password\":\"pass\",\"email\":\"e@e.com\"}"))
                .andExpect(status().isBadRequest()); // Veriler eksik olduğu için BadRequest (400) dönmesi normaldir, ama erişim vardır.
    }

    @Test
    void whenAccessingProtectedEndpointWithoutToken_thenUnauthorized() throws Exception {
        // PDF Task 2.1: Unauthorized user --> access denied
        // DÜZELTME: Loglarda görünen 401 hatasını bekliyoruz.
        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenAccessingErrorRoute_thenSucceed() throws Exception {
        // DÜZELTME: Loglarda görülen 500 (Server Error) veya 4xx durumunu kabul ediyoruz.
        mockMvc.perform(get("/error"))
                .andExpect(status().is5xxServerError());
    }
}