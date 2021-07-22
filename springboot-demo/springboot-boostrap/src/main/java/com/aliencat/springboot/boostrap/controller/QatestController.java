package com.aliencat.springboot.boostrap.controller;

import com.aliencat.springboot.boostrap.response.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class QatestController {

    @RequestMapping("/qatest")
    @ResponseBody
    public JSONResult qatest(String data) {
        log.info("data : " + data);
        try {
            return new JSONResult(200, "请求成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONResult(500, "数据异常", e.toString());
        }
    }

    @GetMapping("/index")
    public String index() {

        return "index";
    }
}
