package com.LibraryManagementSystem.utils.auditing;


import com.LibraryManagementSystem.user.model.BaseUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class ApplicationAuditingAware implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if(authentication == null ||
        ! authentication.isAuthenticated() ||
         authentication instanceof AnonymousAuthenticationToken){
            return Optional.empty();
        }
        BaseUser userPrincipal = (BaseUser) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }
}
