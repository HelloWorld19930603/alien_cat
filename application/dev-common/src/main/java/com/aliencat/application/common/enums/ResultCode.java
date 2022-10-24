package com.aliencat.application.common.enums;

public enum ResultCode {


    SUCCESS(0, "success"),
    SERVER_ERROR(500, "服务器异常"),
    DATABASE_ERROR(502, "数据库错误"),
    UNKNOWN_ERROR(501, "未知错误"),
    NO_PERMISSIONS(1020, "权限不足"),
    SIGN_ERROR(1030, "签名验证失败"),
    PARAM_ERROR(1031, "参数有误"),
    NO_MUST_PARAM(1032, "缺少必传参数"),
    INVALID_PARAM(1033, "非法参数"),
    FAIL(1040, "操作失败"),
    DATA_EXISTS(1041, "记录已存在"),
    FILE_ERROR(1042, "文件读写错误");

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
