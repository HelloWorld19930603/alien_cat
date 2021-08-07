package com.aliencat.springboot.swagger2.controller;

import com.aliencat.springboot.swagger2.pojo.Employee;
import com.aliencat.springboot.swagger2.pojo.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * Swagger控制器
 **/
@RestController
@RequestMapping("/swagger")
@Api(tags = "服务管理", description = "服务管理描述")
public class SwaggerController {

    /**
     * swagger测试
     */
    @GetMapping("/test")
    @ApiOperation(value = "swagger测试", notes = "测试方法的备注说明", httpMethod = "GET")
    public Response test() {
        return Response.success("查询成功", null);
    }

    /**
     * swagger测试对象
     *
     * @ ApiOperation：用在请求的方法上，说明方法的用途、作用
     * value="说明方法的用途、作用"
     * notes="方法的备注说明"
     * httpMethod：Acceptable values are "GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS" and "PATCH".
     */
    @PostMapping("/swaggerBody")
    @ApiOperation(value = "swagger测试对象", notes = "测试方法的备注说明", httpMethod = "POST")
    public Response<Employee> swaggerBody(@RequestBody Employee employee) {
        System.out.println("测试请求对象：" + employee);
        if (employee != null) {
            return Response.success("查询成功", employee);
        }
        return Response.fail("查询失败");
    }

    /**
     * swagger测试参数
     * paramType：Valid values are {@code path}, {@code query}, {@code body}, {@code header} or {@code form}.
     * <p>
     * ApiImplicitParam注解的dataType、paramType
     * dataType="int" 代表请求参数类型为int类型，当然也可以是Map、User、String等；
     * paramType="body" 代表参数应该放在请求的什么地方：
     * header-->放在请求头。请求参数的获取：@RequestHeader(代码中接收注解)
     * query-->用于get请求的参数拼接。请求参数的获取：@RequestParam(代码中接收注解)
     * path（用于restful接口）-->请求参数的获取：@PathVariable(代码中接收注解，拼接到请求地址后边/{name})
     * body-->放在请求体。请求参数的获取：@RequestBody(代码中接收注解)
     * form（不常用）
     */
    @PostMapping("/swaggerParam/{name}")
    @ApiOperation(value = "swagger测试参数", notes = "测试方法的备注说明", httpMethod = "POST")
    @ApiImplicitParam(name = "name", value = "请正确传递用户名", required = true, dataType = "String", paramType = "path")
    public Response<String> swaggerParam(@PathVariable String name) {
        System.out.println("测试请求对象：" + name);
        if (name != null) {
            return Response.success("查询成功", name);
        }
        return Response.fail("查询失败");
    }
}

