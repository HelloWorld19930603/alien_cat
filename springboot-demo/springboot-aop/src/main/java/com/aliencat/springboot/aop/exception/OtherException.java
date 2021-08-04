package com.aliencat.springboot.aop.exception;


public class OtherException extends RuntimeException {
    /**
     * 自定义异常信息
     */
    private String msg;
    /**
     * 状态码
     */
    private int code = 500;


    /**
     * 使用枚举类限制异常信息
     *
     * @param errorCodeEnum 异常封装枚举类
     */
    public OtherException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.toString());
        this.msg = errorCodeEnum.getMsg();
        this.code = errorCodeEnum.getCode();
    }

    /**
     * 获取异常信息
     *
     * @return
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 获取状态码
     *
     * @return
     */
    public int getCode() {
        return code;
    }

}
