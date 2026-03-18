package com.example.BorrowBoxBackend.service;

import com.example.BorrowBoxBackend.config.SupabaseConfig;
import com.example.BorrowBoxBackend.dto.RegisterRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SupabaseAuthService {

    private final SupabaseConfig supabaseConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SupabaseAuthService(SupabaseConfig supabaseConfig) {
        this.supabaseConfig = supabaseConfig;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Object> signUp(RegisterRequest request) throws Exception {
        String url = supabaseConfig.getUrl() + "/auth/v1/signup";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseConfig.getAnonKey());

        Map<String, Object> body = new HashMap<>();
        body.put("email", request.getEmail().trim());
        body.put("password", request.getPassword());

        Map<String, Object> userMetadata = new HashMap<>();
        userMetadata.put("fullName", request.getFullName());
        userMetadata.put("role", request.getRole());

        if ("student".equalsIgnoreCase(request.getRole())) {
            userMetadata.put("studentId", request.getStudentId());
        } else if ("officer".equalsIgnoreCase(request.getRole())) {
            userMetadata.put("orgId", request.getOrgId());
        }

        body.put("data", userMetadata);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        System.out.println("Supabase signUp response: " + response.getBody());

        JsonNode jsonResponse = objectMapper.readTree(response.getBody());

        String supabaseId = null;
        String email = null;

        JsonNode userNode = jsonResponse.get("user");
        if (userNode != null && !userNode.isNull()) {
            JsonNode idNode = userNode.get("id");
            JsonNode emailNode = userNode.get("email");

            if (idNode != null && !idNode.isNull()) {
                supabaseId = idNode.asText();
            }

            if (emailNode != null && !emailNode.isNull()) {
                email = emailNode.asText();
            }
        }

        if ((supabaseId == null || supabaseId.isBlank()) && jsonResponse.has("id")) {
            JsonNode idNode = jsonResponse.get("id");
            if (idNode != null && !idNode.isNull()) {
                supabaseId = idNode.asText();
            }
        }

        if ((email == null || email.isBlank()) && jsonResponse.has("email")) {
            JsonNode emailNode = jsonResponse.get("email");
            if (emailNode != null && !emailNode.isNull()) {
                email = emailNode.asText();
            }
        }

        if (supabaseId == null || supabaseId.isBlank()) {
            throw new RuntimeException("Supabase signup succeeded but no user ID was returned.");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", supabaseId);
        result.put("email", email);

        return result;
    }

    public Map<String, Object> signIn(String email, String password) throws Exception {
        String url = supabaseConfig.getUrl() + "/auth/v1/token?grant_type=password";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseConfig.getAnonKey());

        Map<String, Object> body = new HashMap<>();
        body.put("email", email.trim());
        body.put("password", password);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        JsonNode jsonResponse = objectMapper.readTree(response.getBody());

        Map<String, Object> result = new HashMap<>();
        result.put("access_token", jsonResponse.path("access_token").asText());
        result.put("user", objectMapper.convertValue(jsonResponse.path("user"), Map.class));

        return result;
    }
    public void updateUserProfile(String supabaseId, String fullName) {
        String url = supabaseConfig.getUrl() + "/auth/v1/admin/users/" + supabaseId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseConfig.getServiceRoleKey());
        headers.setBearerAuth(supabaseConfig.getServiceRoleKey());

        Map<String, Object> body = new HashMap<>();
        body.put("user_metadata", Map.of("fullName", fullName));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }
}