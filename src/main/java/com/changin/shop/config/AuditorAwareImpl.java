package com.changin.shop.config;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


@NoArgsConstructor
//로그인된 사용자 정보를 가져오는 방법
public class AuditorAwareImpl implements AuditorAware<String> {

    //로그인하고 있는 사용자의 userName을 가져올 수 있다.
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication != null){
            userId = authentication.getName();
        }

        return Optional.of(userId);
    }
}
