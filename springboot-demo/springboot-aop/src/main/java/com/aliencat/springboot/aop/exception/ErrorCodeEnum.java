package com.aliencat.springboot.aop.exception;

public enum ErrorCodeEnum {

    /**
     * 成功状态码
     */
    SUCCESS(0, "成功"),
    /**
     * 参数为空
     */
    PARAM_EMPTY(1, "参数为空"),
    /**
     * 获取openid为空
     */
    OPENID_IS_EMPTY(2, "获取openid为空"),
    /**
     * 登录失败
     */
    LOGIN_FAILED(3, "登录失败"),
    /**
     * openid未绑定
     */
    OPENID_NOT_BIND(4, "openid未绑定"),
    /**
     * 密码错误
     */
    WRONG_PASSWORD(5, "密码错误"),
    /**
     * 账号不存在
     */
    ACCOUNT_NOT_EXIST(6, "帐号不存在"),
    /**
     * 用户在该系统没有权限
     */
    ACCOUN_NO_AUTHORITY(7, "用户在该系统没有权限"),
    /**
     * base64编码为空
     */
    BASE64_IS_EMPTY(8, "base64编码为空"),
    /**
     * 图片识别类型为空
     */
    RECOGNITION_TYPE_EMPTY(9, "图片识别类型为空"),
    /**
     * 调用接口失败
     */
    CALL_INT_FAIL(10, "调用接口失败"),
    /**
     * 获取权限信息失败
     */
    GET_QX_FAIL(11, "获取权限信息失败"),
    /**
     * 获取信息不存在
     */
    PARAM_NOT_EXIST(12, "获取信息不存在"),
    /**
     * 系统异常
     */
    SYSTEM_ERROR(500, "系统异常");
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 异常信息
     */
    private String msg;

    /**
     * 异常枚举信息
     *
     * @param code 状态码
     * @param msg  信息
     */
    ErrorCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 获取状态码
     *
     * @return
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取信息
     *
     * @return
     */
    public String getMsg() {
        return msg;
    }


    /**
     * 重写toString方法在控制台显示自定义异常信息
     *
     * @return
     */
    @Override
    public String toString() {
        return "[异常码:" + this.code + " 异常信息:" + this.msg + "]";
    }

}
