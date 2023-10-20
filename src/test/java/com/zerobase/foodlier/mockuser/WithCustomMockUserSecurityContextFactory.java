package com.zerobase.foodlier.mockuser;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        long id = annotation.id();
        String email = annotation.email();
        List<String> roles = List.of(annotation.role());

        MemberAuthDto user = MemberAuthDto.builder()
                .id(id)
                .email(email)
                .roles(roles)
                .build();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user, "password", List.of(new SimpleGrantedAuthority(annotation.role())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        return context;
    }
}