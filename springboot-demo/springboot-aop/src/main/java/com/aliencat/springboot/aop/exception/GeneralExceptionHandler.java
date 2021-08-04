package com.aliencat.springboot.aop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 一般的异常处理，所有的API都需要有相同的异常结构。
 * 在这种情况下，实现是非常简单的，我们只需要创建 GeneralExceptionHandler 类，
 * 用 @ControllerAdvice 注解来注解它，并创建所需的 @ExceptionHandler ，
 * 它将处理所有由应用程序抛出的异常，如果它能找到匹配的 @ExceptionHandler，它将相应地进行转换。
 */
@ControllerAdvice
public class GeneralExceptionHandler {

    /**
     * 全局异常处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Error> handleException(Exception ex) {
        MyError myError = MyError.builder()
                .message(ex.getMessage())
                .origin("ControllerAdvice exception test API")
                .code(ErrorCodeEnum.SYSTEM_ERROR.getCode()).build();
        return new ResponseEntity(myError,
                HttpStatus.valueOf(myError.getCode()));


    }
}
