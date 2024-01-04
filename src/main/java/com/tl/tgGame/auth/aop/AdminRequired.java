package com.tl.tgGame.auth.aop;



import com.tl.tgGame.admin.role.entity.PermissionEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminRequired {

    PermissionEnum[] value() default {};

    PermissionEnum[] or() default {};
}
