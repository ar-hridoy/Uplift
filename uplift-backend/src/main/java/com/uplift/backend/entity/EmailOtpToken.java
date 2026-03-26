package com.uplift.backend.entity;

import com.uplift.backend.enums.OtpPurpose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_otp_tokens")
@Getter
@Setter
public class EmailOtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // which user this OTP belongs to
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // 6-digit or similar code
    @Column(nullable = false, length = 10)
    private String code;

    // SIGNUP_VERIFICATION, LOGIN_VERIFICATION, etc.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OtpPurpose purpose;

    // when this OTP expires
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // has it been used already?
    @Column(nullable = false)
    private boolean used = false;

    // audit field
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
