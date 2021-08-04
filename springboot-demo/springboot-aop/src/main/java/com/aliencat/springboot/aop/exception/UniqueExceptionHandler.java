package com.aliencat.springboot.aop.exception;

import com.aliencat.springboot.aop.controller.ExceptionController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 假设我们有一个API，它需要有一个或多个异常以其他格式处理，与其他应用程序的 API 不同。
 * 下面是只针对 ExceptionController 控制器的 @ControllerAdvice 的代码示例
 */
@ControllerAdvice(assignableTypes = ExceptionController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UniqueExceptionHandler {
    @ExceptionHandler(UniqueException.class)
    protected ResponseEntity<Error> handleException(Exception ex) {
        MyError myError = MyError.builder()
                .message(ex.getMessage())
                .origin("ControllerAdvice UniqueException API")
                .code(ErrorCodeEnum.SYSTEM_ERROR.getCode()).build();
        return new ResponseEntity(myError,
                HttpStatus.valueOf(myError.getCode()));
    }
}
