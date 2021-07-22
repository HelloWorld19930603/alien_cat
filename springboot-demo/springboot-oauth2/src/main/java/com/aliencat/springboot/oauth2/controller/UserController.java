package com.aliencat.springboot.oauth2.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通过spring security的角色限制访问受保护的接口
 * 在controller的类或方法上添加注解 @Secured(“ROLE_ADMIN”)
 */
@RestController
@Secured("ROLE_ADMIN")
public class UserController {


    @GetMapping("/user")
    public Authentication getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    @GetMapping("/index")
    @Secured("ROLE_USER")
    public String index() {
        return "index";
    }
}


