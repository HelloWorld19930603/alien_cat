package com.aliencat.springboot.component.base.response;

import com.aliencat.springboot.component.base.code.ResultCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

import static com.aliencat.springboot.component.base.code.ResultCode.FAIL;
import static com.aliencat.springboot.component.base.code.ResultCode.SUCCESS;


/**
 * 请求返回体包装类
 * 注：所有请求返回体中的状态码必须选取ResultCode枚举中定义的值
 *
 * @author cheng
 */
@Getter
@Setter
@Accessors(chain = true)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 6095433538316185017L;

    /**
     * 状态码
     */
    private int code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    private Result() {
    }

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public Result(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    /**
     * 自定义提示信息（用于覆盖ResultCode中定义的message信息）
     *
     * @param resultCode 返回状态码
     * @param message    提示内容
     * @param data       数据对象
     */
    public Result(ResultCode resultCode, String message, T data) {
        this.code = resultCode.getCode();
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功消息
     *
     * @param message 提示内容
     * @param data    数据对象
     * @return 成功消息
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<T>(SUCCESS, message, data);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static <T> Result<T> success() {
        return new Result<T>(SUCCESS);
    }


    /**
     * 返回成功消息
     *
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(SUCCESS, data);
    }


    //------------------------------------------------------------


    /**
     * 返回失败消息
     *
     * @return 失败消息
     */
    public static <T> Result<T> fail() {
        return new Result<T>().setCode(FAIL.getCode()).setMessage(FAIL.getMessage());
    }


    /**
     * 返回失败消息
     *
     * @param resultCode 返回状态码
     * @return 失败消息
     */
    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<T>(resultCode);
    }

    /**
     * 返回失败消息
     *
     * @param data 数据对象
     * @return 失败消息
     */
    public static <T> Result<T> fail(T data) {
        return new Result<T>(FAIL, data);
    }

    /**
     * 返回失败消息
     *
     * @param resultCode 返回状态码
     * @param data       失败详情
     * @return 失败消息
     */
    public static <T> Result<T> fail(ResultCode resultCode, T data) {
        return new Result<T>(resultCode).setData(data);
    }

    /**
     * 返回失败消息
     *
     * @param message 提示内容
     * @param data    失败详情
     * @return 失败消息
     */
    public static <T> Result<T> fail(String message, T data) {
        return new Result<T>(FAIL, message, data);
    }


    @Override
    public String toString() {
        return "{code=" + code + ", message='" + message + ", data=" + data + '}';
    }
}