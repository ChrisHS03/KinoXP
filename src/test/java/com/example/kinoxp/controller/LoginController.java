

package com.example.kinoxp.controller;

import com.example.kinoxp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterCustomer_Success() throws Exception {
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
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testRegisterCustomer_DuplicateUsername() throws Exception {
        String requestBody = """
                {
                    "username": "duplicate",
                    "password": "test123"
                }
                """;

        mockMvc.perform(post("/api/auth/register-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        mockMvc.perform(post("/api/auth/register-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_Success() throws Exception {
        mockMvc.perform(post("/api/auth/register-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""

                        {
                        "username": "logintest",
                        "password": "password123"
                    }
                    """));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "username": "logintest",
                                "password": "password123"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("logintest")))
                .andExpect(jsonPath("$.role", is("CUSTOMER")));
    }

    @Test
    void testLogin_WrongPassword() throws Exception {
        mockMvc.perform(post("/api/auth/register-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "username": "user",
                        "password": "correct"
                    }
                    """));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "username": "user",
                                "password": "wrong"
                            }
                            """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCheckSession_NotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/auth/session"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCheckSession_LoggedIn() throws Exception {
        MockHttpSession session = (MockHttpSession) mockMvc.perform(post("/api/auth/register-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "username": "sessiontest",
                                "password": "test123"
                            }
                            """))
                .andReturn()
                .getRequest()
                .getSession();

        mockMvc.perform(post("/api/auth/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "username": "sessiontest",
                                "password": "test123"
                            }
                            """))
                .andReturn()
                .getRequest()
                .getSession();

        mockMvc.perform(get("/api/auth/session")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("sessiontest")));
    }

    @Test
    void testLogout_Success() throws Exception {
        MockHttpSession session = (MockHttpSession) mockMvc.perform(post("/api/auth/register-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "username": "logouttest",
                                "password": "test123"
                            }
                            """))
                .andReturn()
                .getRequest()
                .getSession();

        mockMvc.perform(post("/api/auth/login")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "username": "logouttest",
                        "password": "test123"
                    }
                    """));

        mockMvc.perform(post("/api/auth/logout")
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/auth/session")
                        .session(session))
                .andExpect(status().isUnauthorized());
    }
}
