package com.yj.bj.exception;

import com.yj.bj.util.YJResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public YJResult handlerSellerException(Exception e){
        e.printStackTrace();
        return YJResult.build("9001","系统错误");
    }

}