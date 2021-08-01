package com.aliencat.communication.rpc.consumer.controller;


import com.aliencat.communication.rpc.api.IUserService;
import com.aliencat.communication.rpc.consumer.process.RpcReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RpcReference
    private IUserService userService;

    @RequestMapping("/{id}")
    public String getUserById(@PathVariable int id) {
        return userService.getById(id).toString();
    }
}
