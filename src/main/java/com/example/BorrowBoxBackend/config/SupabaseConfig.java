package com.example.BorrowBoxBackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupabaseConfig {

    @Value("${supabase.url:https://your-project.supabase.co}")
    private String url;

    @Value("${supabase.anon-key:your-anon-key}")
    private String anonKey;

    @Value("${supabase.jwt-secret:your-jwt-secret}")
    private String jwtSecret;

    public String getUrl() { return url; }
    public String getAnonKey() { return anonKey; }
    public String getJwtSecret() { return jwtSecret; }
}