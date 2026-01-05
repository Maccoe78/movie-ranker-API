package com.example.demo.integration;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_Success() throws Exception {
        String registerRequest = """
            {
                "username": "newuser",
                "password": "password123"
            }
            """;

        // Register via API
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.username").value("newuser"));

        // Verificatie via API: haal user op
        mockMvc.perform(get("/api/auth/users/username/newuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"));

        // Verificatie via API: test login met nieuwe credentials
        String loginRequest = """
            {
                "username": "newuser",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    void registerUser_UsernameAlreadyExists() throws Exception {
        User existingUser = new User("existinguser", passwordEncoder.encode("pass123"));
        userRepository.save(existingUser);

        String registerRequest = """
            {
                "username": "existinguser",
                "password": "newpass456"
            }
            """;

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Username is already taken"));
    }

    @Test
    void loginUser_Success() throws Exception {
        User User = new User("loginuser", passwordEncoder.encode("password123"));
        userRepository.save(User);

        String loginRequest = """
            {
                "username": "loginuser",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.username").value("loginuser"));
    }

    @Test
    void loginUser_InvalidPassword() throws Exception {
        User user = new User("testuser", passwordEncoder.encode("password123"));
        userRepository.save(user);

        String loginRequest = """
            {
                "username": "testuser",
                "password": "wrongpassword"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid password"));
    }

    @Test
    void loginUser_UserNotFound() throws Exception {
        String loginRequest = """
            {
                "username": "nonexistent",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void completeUserWorkflow() throws Exception {
        String registerRequest = """
            {
                "username": "workflowuser",
                "password": "password123"
            }
            """;

        // 1. Register user
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        // 2. Verificatie via API: check of user bestaat
        mockMvc.perform(get("/api/auth/users/username/workflowuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("workflowuser"));

        // 3. Login test
        String loginRequest = """
            {
                "username": "workflowuser",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.username").value("workflowuser"));

        // 4. Test duplicate registration
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Username is already taken"));
    }

    @Test
    void deleteUserById_Success() throws Exception {
        // 1. Setup: maak user aan via repository (setup is OK)
        User user = new User("deleteuser", passwordEncoder.encode("password123"));
        user = userRepository.save(user);
        Long userId = user.getId();

        // 2. Verificatie: user bestaat via API
        mockMvc.perform(get("/api/auth/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("deleteuser"));

        // 3. Delete via API
        mockMvc.perform(delete("/api/auth/users/" + userId))
                .andExpect(status().isOk());

        // 4. Verificatie via API: user is weg (moet 404 geven)
        mockMvc.perform(get("/api/auth/users/" + userId))
                .andExpect(status().isNotFound());
    }
}
