package com.example.BorrowBoxBackend.service;

import com.example.BorrowBoxBackend.dto.LoginRequest;
import com.example.BorrowBoxBackend.dto.RegisterRequest;
import com.example.BorrowBoxBackend.dto.AuthResponse;
import com.example.BorrowBoxBackend.model.Role;
import com.example.BorrowBoxBackend.model.User;
import com.example.BorrowBoxBackend.repository.UserRepository;
import com.example.BorrowBoxBackend.security.JwtUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final SupabaseAuthService supabaseAuthService;

    public AuthService(UserRepository userRepository, JwtUtils jwtUtils,
                       SupabaseAuthService supabaseAuthService) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.supabaseAuthService = supabaseAuthService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        AuthResponse response = new AuthResponse();

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            response.setSuccess(false);
            response.setMessage("Passwords do not match");
            return response;
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            response.setSuccess(false);
            response.setMessage("Email already exists");
            return response;
        }

        if ("student".equalsIgnoreCase(request.getRole())) {
            if (request.getStudentId() == null || request.getStudentId().isEmpty()) {
                response.setSuccess(false);
                response.setMessage("Student ID is required");
                return response;
            }
        } else if ("officer".equalsIgnoreCase(request.getRole())) {
            if (request.getOrgId() == null || request.getOrgId().isEmpty()) {
                response.setSuccess(false);
                response.setMessage("Organization ID is required");
                return response;
            }
        } else {
            response.setSuccess(false);
            response.setMessage("Invalid role selected");
            return response;
        }

        try {
            Map<String, Object> supabaseUser = supabaseAuthService.signUp(request);

            User user = new User();
            user.setEmail(request.getEmail());

            String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            user.setFullName(request.getFullName());

            if ("student".equalsIgnoreCase(request.getRole())) {
                user.setRole(Role.STUDENT);
                user.setStudentId(request.getStudentId());
            } else {
                user.setRole(Role.OFFICER);
                user.setOrgId(request.getOrgId());
            }

            String supabaseId = (String) supabaseUser.get("id");

            if (supabaseId == null || supabaseId.isBlank()) {
                throw new RuntimeException("Supabase ID is missing after signup.");
            }

            user.setSupabaseId(supabaseId);

            User savedUser = userRepository.save(user);
            String token = jwtUtils.generateToken(savedUser);

            response.setSuccess(true);
            response.setToken(token);
            response.setMessage("Registration successful");

            AuthResponse.UserData userData = new AuthResponse.UserData(
                    savedUser.getId(),
                    savedUser.getEmail(),
                    savedUser.getFullName(),
                    savedUser.getRole().name().toLowerCase(),
                    savedUser.getStudentId(),
                    savedUser.getOrgId(),
                    savedUser.getProfilePhoto() != null
            );
            response.setUser(userData);

            return response;

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Registration failed: " + e.getMessage());
            return response;
        }
    }

    public AuthResponse login(LoginRequest request) {
        AuthResponse response = new AuthResponse();

        try {
            Map<String, Object> supabaseResponse = supabaseAuthService.signIn(
                    request.getEmail(),
                    request.getPassword()
            );

            Map<String, Object> supabaseUser = (Map<String, Object>) supabaseResponse.get("user");
            String supabaseUserId = (String) supabaseUser.get("id");

            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            User user;

            if (existingUser.isPresent()) {
                user = existingUser.get();

                if (user.getSupabaseId() == null) {
                    user.setSupabaseId(supabaseUserId);
                    user = userRepository.save(user);
                }
            } else {
                user = new User();
                user.setEmail(request.getEmail());
                user.setPassword("SUPABASE_MANAGED");

                Map<String, Object> userMetadata = (Map<String, Object>) supabaseUser.get("user_metadata");
                if (userMetadata != null) {
                    user.setFullName((String) userMetadata.getOrDefault("fullName", request.getEmail()));

                    String roleValue = (String) userMetadata.getOrDefault("role", "student");
                    if ("officer".equalsIgnoreCase(roleValue)) {
                        user.setRole(Role.OFFICER);
                        user.setOrgId((String) userMetadata.get("orgId"));
                    } else {
                        user.setRole(Role.STUDENT);
                        user.setStudentId((String) userMetadata.get("studentId"));
                    }
                } else {
                    user.setFullName(request.getEmail());
                    user.setRole(Role.STUDENT);
                }

                user.setSupabaseId(supabaseUserId);
                user = userRepository.save(user);
            }

            String token = jwtUtils.generateToken(user);

            response.setSuccess(true);
            response.setToken(token);
            response.setMessage("Login successful");

            AuthResponse.UserData userData = new AuthResponse.UserData(
                    user.getId(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getRole().name().toLowerCase(),
                    user.getStudentId(),
                    user.getOrgId(),
                    user.getProfilePhoto() != null
            );
            response.setUser(userData);

            return response;

        } catch (Exception e) {
            User user = userRepository.findByEmail(request.getEmail()).orElse(null);

            if (user == null) {
                response.setSuccess(false);
                response.setMessage("No account found with that email");
                return response;
            }

            if ("SUPABASE_MANAGED".equals(user.getPassword())) {
                response.setSuccess(false);
                response.setMessage("Invalid password");
                return response;
            }

            if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
                response.setSuccess(false);
                response.setMessage("Invalid password");
                return response;
            }

            String token = jwtUtils.generateToken(user);

            response.setSuccess(true);
            response.setToken(token);
            response.setMessage("Login successful");

            AuthResponse.UserData userData = new AuthResponse.UserData(
                    user.getId(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getRole().name().toLowerCase(),
                    user.getStudentId(),
                    user.getOrgId(),
                    user.getProfilePhoto() != null
            );
            response.setUser(userData);

            return response;
        }
    }
}