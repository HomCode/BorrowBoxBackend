package com.example.BorrowBoxBackend.dto;

public class RegisterRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private String role;
    private String fullName;
    private String studentId;
    private String orgId;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String confirmPassword,
                           String role, String fullName, String studentId, String orgId) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
        this.fullName = fullName;
        this.studentId = studentId;
        this.orgId = orgId;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }
}