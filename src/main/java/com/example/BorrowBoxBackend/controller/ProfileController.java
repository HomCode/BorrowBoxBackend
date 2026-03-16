package com.example.BorrowBoxBackend.controller;

import com.example.BorrowBoxBackend.dto.AuthResponse;
import com.example.BorrowBoxBackend.dto.request.UpdatePasswordRequest;
import com.example.BorrowBoxBackend.dto.request.UpdateProfileRequest;
import com.example.BorrowBoxBackend.model.User;
import com.example.BorrowBoxBackend.repository.UserRepository;
import com.example.BorrowBoxBackend.security.JwtUtils;
import com.example.BorrowBoxBackend.service.SupabaseAuthService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final SupabaseAuthService supabaseAuthService;

    public ProfileController(UserRepository userRepository, JwtUtils jwtUtils,
                             SupabaseAuthService supabaseAuthService) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.supabaseAuthService = supabaseAuthService;
    }

    /**
     * Helper method to extract user from JWT token
     */
    private User getUserFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String username = jwtUtils.getUsernameFromToken(token);
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * 3. Profile API - Get user profile
     */
    @GetMapping
    public ResponseEntity<AuthResponse> getProfile(@RequestHeader("Authorization") String token) {
        AuthResponse response = new AuthResponse();

        try {
            User user = getUserFromToken(token);

            if (user == null) {
                response.setSuccess(false);
                response.setMessage("User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            AuthResponse.UserData userData = new AuthResponse.UserData(
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getRole(),
                    user.getStudentId(),
                    user.getOrgId(),
                    user.getProfilePhoto() != null
            );

            response.setSuccess(true);
            response.setUser(userData);
            response.setMessage("Profile retrieved successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error retrieving profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 4. Edit Profile API - Update user profile
     */
    @PutMapping
    public ResponseEntity<AuthResponse> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateProfileRequest request) {
        AuthResponse response = new AuthResponse();

        try {
            User user = getUserFromToken(token);

            if (user == null) {
                response.setSuccess(false);
                response.setMessage("User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (request.getFullName() != null && !request.getFullName().isEmpty()) {
                user.setFullName(request.getFullName());
            }

            if ("student".equals(user.getRole()) && request.getStudentId() != null) {
                user.setStudentId(request.getStudentId());
            } else if ("officer".equals(user.getRole()) && request.getOrgId() != null) {
                user.setOrgId(request.getOrgId());
            }

            User updatedUser = userRepository.save(user);

            AuthResponse.UserData userData = new AuthResponse.UserData(
                    updatedUser.getId(),
                    updatedUser.getUsername(),
                    updatedUser.getFullName(),
                    updatedUser.getRole(),
                    updatedUser.getStudentId(),
                    updatedUser.getOrgId(),
                    updatedUser.getProfilePhoto() != null
            );

            response.setSuccess(true);
            response.setUser(userData);
            response.setMessage("Profile updated successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error updating profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 5. Edit Password API - Change user password
     */
    @PutMapping("/password")
    public ResponseEntity<AuthResponse> updatePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdatePasswordRequest request) {
        AuthResponse response = new AuthResponse();

        try {
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                response.setSuccess(false);
                response.setMessage("New passwords do not match");
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getNewPassword().length() < 6) {
                response.setSuccess(false);
                response.setMessage("Password must be at least 6 characters");
                return ResponseEntity.badRequest().body(response);
            }

            User user = getUserFromToken(token);

            if (user == null) {
                response.setSuccess(false);
                response.setMessage("User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!user.getPassword().equals("SUPABASE_MANAGED")) {
                if (!BCrypt.checkpw(request.getCurrentPassword(), user.getPassword())) {
                    response.setSuccess(false);
                    response.setMessage("Current password is incorrect");
                    return ResponseEntity.badRequest().body(response);
                }
            }

            String hashedPassword = BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            userRepository.updatePassword(user.getId(), hashedPassword);

            response.setSuccess(true);
            response.setMessage("Password updated successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error updating password: " + e.getMessage());
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error updating password: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 6. Upload Photo API - Upload and store user image
     */
    @PostMapping("/photo")
    public ResponseEntity<AuthResponse> uploadPhoto(
            @RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file) {
        AuthResponse response = new AuthResponse();

        try {
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("image/jpeg") &&
                    !contentType.equals("image/jpg") && !contentType.equals("image/png"))) {
                response.setSuccess(false);
                response.setMessage("Only JPG and PNG images are allowed");
                return ResponseEntity.badRequest().body(response);
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                response.setSuccess(false);
                response.setMessage("File size must be less than 5MB");
                return ResponseEntity.badRequest().body(response);
            }

            User user = getUserFromToken(token);

            if (user == null) {
                response.setSuccess(false);
                response.setMessage("User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            byte[] imageBytes = file.getBytes();

            userRepository.updatePhoto(user.getId(), imageBytes, contentType);

            response.setSuccess(true);
            response.setMessage("Photo uploaded successfully");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error reading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error uploading photo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get user photo
     */
    @GetMapping("/photo")
    public ResponseEntity<byte[]> getPhoto(@RequestHeader("Authorization") String token) {
        try {
            User user = getUserFromToken(token);

            if (user == null || user.getProfilePhoto() == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] photoBytes = user.getProfilePhoto();

            String contentType = user.getPhotoContentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "image/jpeg";
            }

            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .header("Content-Length", String.valueOf(photoBytes.length))
                    .body(photoBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete photo
     */
    @DeleteMapping("/photo")
    public ResponseEntity<AuthResponse> deletePhoto(@RequestHeader("Authorization") String token) {
        AuthResponse response = new AuthResponse();

        try {
            User user = getUserFromToken(token);

            if (user == null) {
                response.setSuccess(false);
                response.setMessage("User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            user.setProfilePhoto(null);
            user.setPhotoContentType(null);
            user.setPhotoUpdatedAt(null);

            userRepository.save(user);

            response.setSuccess(true);
            response.setMessage("Photo deleted successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error deleting photo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}