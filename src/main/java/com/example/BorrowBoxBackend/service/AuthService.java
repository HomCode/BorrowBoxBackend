package com.example.BorrowBoxBackend.service;

import com.example.BorrowBoxBackend.dto.LoginRequest;
import com.example.BorrowBoxBackend.dto.RegisterRequest;
import com.example.BorrowBoxBackend.dto.AuthResponse;
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

        // Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            response.setSuccess(false);
            response.setMessage("Passwords do not match");
            return response;
        }

        // Check if user exists
        if (userRepository.existsByUsername(request.getUsername())) {
            response.setSuccess(false);
            response.setMessage("Username already exists");
            return response;
        }

        // Validate role-specific fields
        if ("student".equals(request.getRole())) {
            if (request.getStudentId() == null || request.getStudentId().isEmpty()) {
                response.setSuccess(false);
                response.setMessage("Student ID is required");
                return response;
            }
        } else if ("officer".equals(request.getRole())) {
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
            System.out.println("=== REGISTRATION ATTEMPT ===");
            System.out.println("Username: " + request.getUsername());

            // Register with Supabase
            Map<String, Object> supabaseUser = supabaseAuthService.signUp(request);
            System.out.println("✅ Supabase registration successful. User ID: " + supabaseUser.get("id"));

            // Create local user
            User user = new User();
            user.setUsername(request.getUsername());

            // Hash password with jBCrypt
            String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);
            System.out.println("Password hashed for local storage");

            user.setFullName(request.getFullName());
            user.setRole(request.getRole());

            if ("student".equals(request.getRole())) {
                user.setStudentId(request.getStudentId());
            } else {
                user.setOrgId(request.getOrgId());
            }

            user.setSupabaseId((String) supabaseUser.get("id"));

            User savedUser = userRepository.save(user);
            System.out.println("✅ User saved to local database with ID: " + savedUser.getId());

            // Generate token
            String token = jwtUtils.generateToken(savedUser);

            // Create response
            response.setSuccess(true);
            response.setToken(token);
            response.setMessage("Registration successful");

            AuthResponse.UserData userData = new AuthResponse.UserData(
                    savedUser.getId(),
                    savedUser.getUsername(),
                    savedUser.getFullName(),
                    savedUser.getRole(),
                    savedUser.getStudentId(),
                    savedUser.getOrgId()
            );
            response.setUser(userData);

            return response;

        } catch (Exception e) {
            System.err.println("❌ Registration failed: " + e.getMessage());
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Registration failed: " + e.getMessage());
            return response;
        }
    }

    public AuthResponse login(LoginRequest request) {
        AuthResponse response = new AuthResponse();

        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Username: " + request.getUsername());
        System.out.println("Password: " + request.getPassword());

        try {
            // First, try to authenticate with Supabase
            System.out.println("Attempting Supabase authentication...");
            Map<String, Object> supabaseResponse = supabaseAuthService.signIn(
                    request.getUsername(),
                    request.getPassword()
            );
            System.out.println("✅ Supabase authentication successful!");
            System.out.println("Access token received: " + supabaseResponse.get("access_token"));

            // Get user info from Supabase response
            Map<String, Object> supabaseUser = (Map<String, Object>) supabaseResponse.get("user");
            String supabaseUserId = (String) supabaseUser.get("id");
            System.out.println("Supabase User ID: " + supabaseUserId);

            // Find or create user in local database
            Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
            User user;

            if (existingUser.isPresent()) {
                user = existingUser.get();
                System.out.println("✅ User found in local database with role: " + user.getRole());

                // Update Supabase ID if not set
                if (user.getSupabaseId() == null) {
                    user.setSupabaseId(supabaseUserId);
                    user = userRepository.save(user);
                    System.out.println("Updated user with Supabase ID");
                }
            } else {
                System.out.println("⚠️ User not found in local database. Creating from Supabase data...");

                // Create new user from Supabase data
                user = new User();
                user.setUsername(request.getUsername());

                // We don't need to store the actual password since Supabase handles auth
                user.setPassword("SUPABASE_MANAGED");

                // Get user metadata if available
                Map<String, Object> userMetadata = (Map<String, Object>) supabaseUser.get("user_metadata");
                if (userMetadata != null) {
                    user.setFullName((String) userMetadata.getOrDefault("fullName", request.getUsername()));
                    user.setRole((String) userMetadata.getOrDefault("role", "student"));

                    if ("student".equals(user.getRole())) {
                        user.setStudentId((String) userMetadata.get("studentId"));
                    } else if ("officer".equals(user.getRole())) {
                        user.setOrgId((String) userMetadata.get("orgId"));
                    }
                } else {
                    // Default values
                    user.setFullName(request.getUsername());
                    user.setRole("student");
                }

                user.setSupabaseId(supabaseUserId);
                user = userRepository.save(user);
                System.out.println("✅ New user created in local database with ID: " + user.getId());
            }

            // Generate JWT token for your app
            String token = jwtUtils.generateToken(user);
            System.out.println("✅ JWT token generated");

            // Create response
            response.setSuccess(true);
            response.setToken(token);
            response.setMessage("Login successful");

            AuthResponse.UserData userData = new AuthResponse.UserData(
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getRole(),
                    user.getStudentId(),
                    user.getOrgId()
            );
            response.setUser(userData);

            return response;

        } catch (Exception e) {
            System.err.println("❌ Login failed: " + e.getMessage());
            e.printStackTrace();

            // Optional: Fall back to local authentication if Supabase fails
            // You can remove this if you want to rely only on Supabase
            System.out.println("Attempting fallback to local authentication...");

            User user = userRepository.findByUsername(request.getUsername()).orElse(null);

            if (user != null && BCrypt.checkpw(request.getPassword(), user.getPassword())) {
                System.out.println("✅ Local authentication successful (fallback)");

                String token = jwtUtils.generateToken(user);

                response.setSuccess(true);
                response.setToken(token);
                response.setMessage("Login successful (local)");

                AuthResponse.UserData userData = new AuthResponse.UserData(
                        user.getId(),
                        user.getUsername(),
                        user.getFullName(),
                        user.getRole(),
                        user.getStudentId(),
                        user.getOrgId()
                );
                response.setUser(userData);

                return response;
            }

            response.setSuccess(false);
            response.setMessage("Invalid credentials");
            return response;
        }
    }
}