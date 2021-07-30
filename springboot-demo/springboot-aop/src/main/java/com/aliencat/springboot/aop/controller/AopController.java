package com.aliencat.springboot.aop.controller;

import com.aliencat.springboot.aop.annotation.BusinessLogAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RestController
@Slf4j
public class AopController {

    @RequestMapping("/")
    public Object index(HttpServletRequest request) {
        Enumeration<String> enums = request.getParameterNames();
        List<String> params = new ArrayList<>();
        while (enums.hasMoreElements()) {
            String paraName = enums.nextElement();
            String param = paraName + ":" + request.getParameter(paraName);
            log.info(param);
            params.add(param);
        }
        return params;
    }

    @RequestMapping("/log")
    @BusinessLogAnnotation("查看log")
    public Object log(HttpServletRequest request) {
        return request.getRequestURL();
    }
}
