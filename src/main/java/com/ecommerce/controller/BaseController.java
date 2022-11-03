package com.ecommerce.controller;

import com.ecommerce.error.BusinessException;
import com.ecommerce.error.EmBusinessError;
import com.ecommerce.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BaseController
 * @Description 所有controller的基类，包含handle exception等
 * @Author Steven
 * @Date 2022/11/2
 * @Version 1.0
 **/
public class BaseController {

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";

    // 定义exception handler解决未被controller层吸收的异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    // TODO: 探索ResponseBody的具体覆盖范围
    public Object handlerException(HttpServletRequest request, Exception ex){

        // set common return type's data
        Map<String, Object> responseData = new HashMap<>();

        if(ex instanceof BusinessException){
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        }else{
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }

        return CommonReturnType.create(responseData, "fail");
    }
}
