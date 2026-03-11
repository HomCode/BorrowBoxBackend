package com.example.BorrowBoxBackend.service;

import com.example.BorrowBoxBackend.dto.RegisterRequest;  // ✅ Fix: Use correct package
import com.example.BorrowBoxBackend.config.SupabaseConfig;  // ✅ Fix: Use correct package
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

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
        body.put("email", request.getUsername());
        body.put("password", request.getPassword());

        Map<String, Object> userMetadata = new HashMap<>();
        userMetadata.put("fullName", request.getFullName());
        userMetadata.put("role", request.getRole());

        if ("student".equals(request.getRole())) {
            userMetadata.put("studentId", request.getStudentId());
        } else if ("officer".equals(request.getRole())) {
            userMetadata.put("orgId", request.getOrgId());
        }

        body.put("data", userMetadata);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        JsonNode jsonResponse = objectMapper.readTree(response.getBody());

        Map<String, Object> result = new HashMap<>();
        result.put("id", jsonResponse.path("id").asText());
        result.put("email", jsonResponse.path("email").asText());

        return result;
    }

    public Map<String, Object> signIn(String email, String password) throws Exception {
        String url = supabaseConfig.getUrl() + "/auth/v1/token?grant_type=password";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseConfig.getAnonKey());

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        JsonNode jsonResponse = objectMapper.readTree(response.getBody());

        Map<String, Object> result = new HashMap<>();
        result.put("access_token", jsonResponse.path("access_token").asText());
        result.put("user", jsonResponse.path("user"));

        return result;
    }
}