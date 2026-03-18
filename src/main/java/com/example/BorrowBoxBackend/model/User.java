package com.example.BorrowBoxBackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username", unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "supabase_id")
    private String supabaseId;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "profile_photo", columnDefinition = "bytea")
    private byte[] profilePhoto;

    @Column(name = "photo_content_type", length = 100)
    private String photoContentType;

    @Column(name = "photo_updated_at")
    private LocalDateTime photoUpdatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User() {}

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public Role getRole() {
        return role;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getSupabaseId() {
        return supabaseId;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public LocalDateTime getPhotoUpdatedAt() {
        return photoUpdatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setSupabaseId(String supabaseId) {
        this.supabaseId = supabaseId;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public void setPhotoUpdatedAt(LocalDateTime photoUpdatedAt) {
        this.photoUpdatedAt = photoUpdatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}