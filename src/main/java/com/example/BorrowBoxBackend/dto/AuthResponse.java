package com.example.BorrowBoxBackend.dto;

public class AuthResponse {
    private boolean success;
    private UserData user;
    private String token;
    private String message;

    public AuthResponse() {}

    public AuthResponse(boolean success, UserData user, String token, String message) {
        this.success = success;
        this.user = user;
        this.token = token;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public UserData getUser() { return user; }
    public void setUser(UserData user) { this.user = user; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static class UserData {
        private String id;
        private String username;
        private String fullName;
        private String role;
        private String studentId;
        private String orgId;
        private boolean hasPhoto;

        public UserData() {}

        public UserData(String id, String username, String fullName, String role,
                        String studentId, String orgId) {
            this.id = id;
            this.username = username;
            this.fullName = fullName;
            this.role = role;
            this.studentId = studentId;
            this.orgId = orgId;
            this.hasPhoto = false;
        }

        public UserData(String id, String username, String fullName, String role,
                        String studentId, String orgId, boolean hasPhoto) {
            this.id = id;
            this.username = username;
            this.fullName = fullName;
            this.role = role;
            this.studentId = studentId;
            this.orgId = orgId;
            this.hasPhoto = hasPhoto;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }

        public String getOrgId() { return orgId; }
        public void setOrgId(String orgId) { this.orgId = orgId; }

        public boolean isHasPhoto() { return hasPhoto; }
        public void setHasPhoto(boolean hasPhoto) { this.hasPhoto = hasPhoto; }
    }
}