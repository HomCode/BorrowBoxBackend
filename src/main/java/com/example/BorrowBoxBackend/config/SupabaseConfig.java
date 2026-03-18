package com.example.BorrowBoxBackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupabaseConfig {

    @Value("${supabase.url}")
    private String url;

    @Value("${supabase.anon-key}")
    private String anonKey;

    @Value("${supabase.jwt-secret}")
    private String jwtSecret;

    @Value("${supabase.service-role-key}")
    private String serviceRoleKey;

    public String getUrl() {
        return url;
    }

    public String getAnonKey() {
        return anonKey;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public String getServiceRoleKey() {
        return serviceRoleKey;
    }
}