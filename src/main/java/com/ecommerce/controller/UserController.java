package com.ecommerce.controller;

import com.ecommerce.controller.viewobject.UserVO;
import com.ecommerce.error.BusinessException;
import com.ecommerce.error.EmBusinessError;
import com.ecommerce.response.CommonReturnType;
import com.ecommerce.service.UserService;
import com.ecommerce.service.model.UserModel;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.ResponseWrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Steven
 * @Date 2022/11/2
 * @Version 1.0
 **/
@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    // 并不是每个thread单例，本质是一个proxy,里面有threadLocal方法，保证多线程的时候不串
    private HttpServletRequest httpServletRequest;


    @RequestMapping("/getotp")
    public CommonReturnType getOtp(@RequestParam(name="telephone")String telephone){
        // 按照规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        // 将OTP验证码同对应用户手机号关联，使用http session的方式绑定他的手机号与OTP CODE
        httpServletRequest.getSession().setAttribute(telephone, otpCode);

        // 讲OTP发给用户手机
        System.out.println("telephone = "+ telephone + " & otp code = " + otpCode);

        return CommonReturnType.create(null);
    }


    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException{
        // 调用service服务获取对应id的用户对象，并返回给前端
        UserModel userModel = userService.getUserById(id);

        // if user do not exists
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        UserVO userVO = convertFormModel(userModel);

        return CommonReturnType.create(userVO);
    }

    private UserVO convertFormModel(UserModel userModel){
        if(userModel == null){
            return null;
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);

        return userVO;
    }

}
