package com.aliencat.springboot.nebula.ocean.exception;


import com.aliencat.springboot.nebula.ocean.common.ResponseService;


public class NebulaException extends RuntimeException {

    private final String code;

    public NebulaException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public NebulaException(Throwable throwable) {
        super(throwable);
        this.code = throwable.getMessage();
    }

    public NebulaException(ResponseService responseService) {
        super(responseService.getResponseMessage());
        this.code = responseService.getResponseCode();
    }

}
