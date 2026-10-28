package com.applogin.login.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name="tbl_users")
@Data //getters and setters (for all non final fields), toString(), equals() and hashCode() implementations, @RequiredArgsConstructor
@Builder // It allows you to create objects using a clean, fluent, and readable API chain, which is incredibly useful for classes with many fields.
@AllArgsConstructor //Generates a constructor with one parameter for every field in the class, in the exact order they are declared.
@NoArgsConstructor //Generates a no-argument (default) constructor, Hibernate/JPA (for database entities) and libraries like Jackson (for JSON parsing/deserialization) strictly require a no-argument constructor to instantiate objects before populating them with data.
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String userId;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String verifyOtp;
    private boolean isAccountVerified;
    private Long verifyOtpExpireAt;
    private String resetOtp;
    private Long resetOtpExpireAt;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
}
