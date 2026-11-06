package com.example.security;

import java.lang.annotation.*;
import com.example.domain.Role;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AllowedRoles {
    Role[] value();
}