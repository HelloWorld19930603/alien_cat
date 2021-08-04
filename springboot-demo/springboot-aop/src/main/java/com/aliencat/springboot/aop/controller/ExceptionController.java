package com.aliencat.springboot.aop.controller;

import com.aliencat.springboot.aop.exception.ErrorCodeEnum;
import com.aliencat.springboot.aop.exception.MyError;
import com.aliencat.springboot.aop.exception.OtherException;
import com.aliencat.springboot.aop.exception.UniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * springboot aop全局异常异常
 * <p>
 * 我们可以在 ExceptionController 内部添加 @ExceptionHandler 来处理 OtherException ，
 * 或者为 ExceptionController 创建新的@ControllerAdvice，
 * 以备我们也想在其他 API 中处理 OtherException。
 */
@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @ExceptionHandler(OtherException.class)
    protected ResponseEntity<Error> handleException(OtherException ex) {
        MyError myError = MyError.builder()
                .message(ex.getMessage())
                .origin("ExceptionHandler exception test API")
                .code(ErrorCodeEnum.SYSTEM_ERROR.getCode())
                .build();
        return new ResponseEntity(myError,
                HttpStatus.valueOf(myError.getCode()));
    }

    @RequestMapping
    public String testException(int type) throws Exception {
        switch (type) {
            case 1:
                throw new Exception(ErrorCodeEnum.SYSTEM_ERROR.getMsg());
            case 2:
                throw new OtherException(ErrorCodeEnum.ACCOUN_NO_AUTHORITY);
            case 3:
                throw new UniqueException(ErrorCodeEnum.ACCOUNT_NOT_EXIST);
        }
        return "success";
    }
}
