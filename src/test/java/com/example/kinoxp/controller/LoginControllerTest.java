package com.example.kinoxp.controller;

import com.example.kinoxp.model.Role;
import com.example.kinoxp.model.User;
import com.example.kinoxp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = LoginController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class LoginControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private MockHttpSession session;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "password123", Role.CUSTOMER);
        testUser.setId(1);

        session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());
        session.setAttribute("username", testUser.getUsername());
        session.setAttribute("role", testUser.getRole().toString());
    }

    @Test
    void testRegisterCustomer_Success() throws Exception {
        when(userService.registerUser(eq("testuser"), eq("test123"), eq(Role.CUSTOMER)))
                .thenReturn(testUser);

        String requestBody = """
                {
                    "username": "testuser",
                    "password": "test123"
                }
                """;

        mockMvc.perform(post("/api/auth/register-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.role", is("CUSTOMER")))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testRegisterCustomer_DuplicateUsername() throws Exception {
        when(userService.registerUser(eq("duplicate"), eq("test123"), eq(Role.CUSTOMER)))
                .thenThrow(new IllegalArgumentException("Username already exists"));

        String requestBody = """
                {
                    "username": "duplicate",
                    "password": "test123"
                }
                """;

        mockMvc.perform(post("/api/auth/register-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    @Test
    void testLogin_Success() throws Exception {
        when(userService.loginUser(eq("logintest"), eq("password123")))
                .thenReturn(Optional.of(testUser));

        String requestBody = """
                {
                    "username": "logintest",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.role", is("CUSTOMER")))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testLogin_WrongPassword() throws Exception {
        when(userService.loginUser(eq("user"), eq("wrong")))
                .thenReturn(Optional.empty());

        String requestBody = """
                {
                    "username": "user",
                    "password": "wrong"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void testCheckSession_NotLoggedIn() throws Exception {
        MockHttpSession emptySession = new MockHttpSession();

        mockMvc.perform(get("/api/auth/session")
                        .session(emptySession))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Not logged in"));
    }

    @Test
    void testCheckSession_LoggedIn() throws Exception {
        mockMvc.perform(get("/api/auth/session")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.role", is("CUSTOMER")))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testLogout_Success() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));
    }
}
