package com.uplift.backend.repository;

import com.uplift.backend.entity.EmailOtpToken;
import com.uplift.backend.entity.User;
import com.uplift.backend.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailOtpTokenRepository extends JpaRepository<EmailOtpToken, Long> {

    Optional<EmailOtpToken> findTopByUserAndPurposeAndUsedFalseOrderByIdDesc(
            User user, OtpPurpose purpose
    );
}
