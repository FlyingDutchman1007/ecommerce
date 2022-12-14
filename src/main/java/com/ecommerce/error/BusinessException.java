package com.ecommerce.error;

/**
 * @ClassName BusinessException
 * @Description 包装器业务异常类实现。在controller层产生exception时，最终会有business exception来处理
 * @Author Steven
 * @Date 2022/11/2
 * @Version 1.0
 **/
public class BusinessException extends Exception implements CommonError{

    private CommonError commonError;

    // 直接接收emBusinessError的传参，用于构造异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    // 接收自定义errorMsg的方式，构造业务异常
    public BusinessException(CommonError commonError, String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this.commonError;
    }
}