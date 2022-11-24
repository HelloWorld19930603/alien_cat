package com.aliencat.springboot.component.base.code;

/**
 * 自定义业务状态码
 * 注：除操作成功状态码外，其它操作码统一使用4位整形表示(因为http相关状态码通常都是3位整数，避免冲突)
 * @author cheng
 */
public enum ResultCode {

    SUCCESS(0, "操作成功"),

    FAIL(1, "操作失败"),

    /**
     * 10开头为后台逻辑异常
     */
    SERVER_ERROR(1001, "服务器异常"),
    UNKNOWN_ERROR(1002, "未知错误"),

    /**
     * 20开头为权限类异常
     */
    NO_PERMISSIONS(2001, "权限不足"),
    SIGN_ERROR(2002, "签名验证失败"),
    VERIFICATION_CODE_ERROR(2003, "验证码错误"),
    VERIFICATION_CODE_EXPIRE(2004, "验证码已过期"),

    /**
     * 30开头为前台参数类异常
     */
    PARAM_ERROR(3001, "参数有误"),
    NO_MUST_PARAM(3002, "缺少必传参数"),
    INVALID_PARAM(3003, "非法参数"),
    DUPLICATE_ID_CARD(3004, "身份证号重复"),


    /**
     * 40开头为IO类异常
     */
    DATABASE_ERROR(4001, "数据库错误"),
    DATA_EXISTS(4002, "记录已存在"),
    FILE_ERROR(4003, "文件读写错误");

    int code;
    String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
