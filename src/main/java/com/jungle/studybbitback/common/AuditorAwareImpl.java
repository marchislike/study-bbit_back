package com.jungle.studybbitback.common;

import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

// createdBy, ModifiedBy 자동생성 위한 코드
@Component
public class AuditorAwareImpl implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            return Optional.empty();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return Optional.of(userDetails.getMemberId());
    }
}