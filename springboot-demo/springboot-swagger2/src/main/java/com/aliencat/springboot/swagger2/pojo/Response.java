package com.aliencat.springboot.swagger2.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 结果集封装
 **/
@Data
@Component
public class Response<T> {
    private static ResponseCode responseCode;
    /**
     * 提示消息
     */
    private String message;

    /**
     * 具体返回的数据
     */
    private T data;

    /**
     * 状态码
     */
    private String code;

    private Response(String code, String message, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    private Response(String code, String msg) {
        this.message = msg;
        this.code = code;
    }

    @Autowired
    public Response(ResponseCode responseCode) {
        Response.responseCode = responseCode;
    }

    /**
     * 返回成功Response对象
     */
    public static <T> Response<T> success(String successMessage, T data) {
        return new Response<>(responseCode.getSuccessCode(), successMessage, data);
    }

    /**
     * 返回错误Response对象
     */
    public static <T> Response<T> fail(String errorMessage) {
        return new Response<>(responseCode.getErrorCode(), errorMessage);
    }
}
