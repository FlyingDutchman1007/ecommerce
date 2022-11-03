package com.ecommerce.response;

/**
 * @ClassName CommonReturnType
 * @Description Common return type from backend to frontend. Could handle error messages. Has 2 status, success or fail
 * @Author Steven
 * @Date 2022/11/2
 * @Version 1.0
 **/
public class CommonReturnType {

    private String status;
    private Object data;

    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result, "success");
    }

    public static CommonReturnType create(Object result, String status){

        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
