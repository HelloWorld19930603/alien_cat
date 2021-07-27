package com.aliencat.communication.user.controller;

import com.aliencat.communication.netty.annotation.Action;
import com.aliencat.communication.user.model.User;
import org.springframework.stereotype.Controller;


@Controller
public class UserController {


    @Action("saveUser")
    public Object save(User user) {
        System.out.println(user.getName());
        return user.getName();
    }

}
