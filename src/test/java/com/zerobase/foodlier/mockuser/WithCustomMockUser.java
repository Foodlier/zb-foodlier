package com.zerobase.foodlier.mockuser;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class)
public @interface WithCustomMockUser {


    long id() default 1L;
    String email() default "test@test.com";
    String role() default "ROLE_USER";
}
