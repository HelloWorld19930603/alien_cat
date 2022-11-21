
package com.aliencat.springboot.nebula.ocean.exception;


import com.aliencat.springboot.nebula.ocean.enums.ErrorEnum;

/**
 * Description  NebulaVersionConflictException is used for
 *
 * @author Anyzm
 * Date  2021/8/9 - 10:54
 * @version 1.0.0
 */
public class NebulaVersionConflictException extends NebulaExecuteException {


    public NebulaVersionConflictException(int code, String msg) {
        super(code, msg);
    }

    public NebulaVersionConflictException(String code, String msg) {
        super(code, msg);
    }

    public NebulaVersionConflictException(int code, String msg, Throwable cause) {
        super(code, msg, cause);
    }

    public NebulaVersionConflictException(String code, String msg, Throwable cause) {
        super(code, msg, cause);
    }

    public NebulaVersionConflictException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public NebulaVersionConflictException(ErrorEnum errorEnum, Throwable cause) {
        super(errorEnum, cause);
    }

}
