package com.ecommerce.error;

public enum EmBusinessError implements CommonError {

    // 通用错误类型，100xx，参数相关问题
    PARAMETER_VALIDATION_ERROR(10001, "parameter invalid"),
    UNKNOWN_ERROR(10002, "unknown error"),

    // 200xx error code handles user-related errors
    USER_NOT_EXIST(20001, "User does not exist")
    ;

    private EmBusinessError(int errCode, String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
