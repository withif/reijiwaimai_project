package com.hjnu.Common;

import lombok.extern.slf4j.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

/**
 * 全局异常处理
 */
@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class MyExceptionHandler {
    /**
     * 异常处理方法,处理SQLIntegrityConstraintViolationException异常
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        log.error(exception.getMessage());
        if(exception.getMessage().contains("Duplicate entry")){
            String[] allErrors = exception.getMessage().split(" ");
            String mes=allErrors[2]+"已存在";
            return  R.error(mes);

        }
        return R.error("未知错误!");
    }

    /**
     * 异常处理方法,处理自定义的CliException异常
     * @param exception
     * @return
     */
    @ExceptionHandler(CliException.class)
    public R<String> exceptionHandler(CliException exception){
        log.error("异常信息:"+exception.getMessage());
        return R.error(exception.getMessage());
    }


}
