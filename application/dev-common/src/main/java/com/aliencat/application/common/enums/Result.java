package com.aliencat.application.common.enums;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 操作消息提醒
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

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(ResultCode code, T data){
        this.code = code.getCode();
        this.message = code.message;
        this.data = data;
    }
    /**
     * 返回成功消息
     *
     * @param message  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<T>().setCode(0).setData(data).setMessage(message);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static <T> Result<T> success() {
        return new Result<T>().setCode(0);
    }

    /**
     * 返回成功消息
     *
     * @param message 返回内容
     * @return 成功消息
     */
    public static <T> Result<T> success(String message) {
        return new Result<>(0, message);
    }

    /**
     * 返回成功消息
     *
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "操作成功", data);
    }
    //------------------------------------------------------------

    /**
     * 返回失败消息
     *
     * @param message  返回内容
     * @param data 数据对象
     * @return 失败消息
     */
    public static <T> Result<T> fail(String message, T data) {
        return new Result<T>().setCode(1).setData(data).setMessage(message);
    }

    /**
     * 返回失败消息
     *
     * @return 失败消息
     */
    public static <T> Result<T> fail() {
        return new Result<T>().setCode(1);
    }

    /**
     * 返回失败消息
     *
     * @param message 返回内容
     * @return 失败消息
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(1, message);
    }

    /**
     * 返回失败消息
     *
     * @param data 数据对象
     * @return 失败消息
     */
    public static <T> Result<T> fail(T data) {
        return new Result<>(1, "操作失败", data);
    }

    @Override
    public String toString() {
        return "{code=" + code + ", message='" + message + ", data=" + data + '}';
    }
}