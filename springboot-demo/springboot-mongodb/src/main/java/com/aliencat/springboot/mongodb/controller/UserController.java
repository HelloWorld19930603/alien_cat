package com.aliencat.springboot.mongodb.controller;

import com.aliencat.springboot.mongodb.entity.User;
import com.aliencat.springboot.mongodb.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@EnableSwagger2
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 增加
     * @param user
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation (value = "insert",notes = "插入")
    public User insertUser(User user) {
        return userService.save(user);
    }

    /**
     * 查询
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    @ApiOperation (value = "get",notes = "查询")
    public User getUserById(@PathVariable String id) {
        return userService.getById(id);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation (value = "delete",notes = "删除")
    public User deleteUserById(@PathVariable String id) {
        return userService.deleteById(id);
    }

    /**
     * 修改
     * @param user
     * @return
     */
    @PutMapping("/update")
    @ApiOperation (value = "update",notes = "修改")
    public User updateUser(User user) {
        return userService.update(user);
    }

    /**
     * 查询所有的用户
     * @return
     */
    @GetMapping("/get/all")
    @ApiOperation(value = "getAll",notes = "获取所有")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * 分页查询用户
     * @param userName
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/select/{page}/{size}")
    @ApiOperation (value = "select",notes = "分页查询")
    public Map<String, Object> selectUserByProperty(String userName, @PathVariable int page, @PathVariable int size) {
        return userService.query(userName,page,size);
    }
}
