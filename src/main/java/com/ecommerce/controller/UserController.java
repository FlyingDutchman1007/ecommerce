package com.ecommerce.controller;

import com.alibaba.druid.util.StringUtils;
import com.ecommerce.controller.viewobject.UserVO;
import com.ecommerce.error.BusinessException;
import com.ecommerce.error.EmBusinessError;
import com.ecommerce.response.CommonReturnType;
import com.ecommerce.service.UserService;
import com.ecommerce.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @ClassName UserController
 * @Description 控制用户行为 controller
 * @Author Steven
 * @Date 2022/11/2
 * @Version 1.0
 **/
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*") //TODO: 确认cross origin是否不安全
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    // 并不是每个thread单例，本质是一个proxy,里面有threadLocal方法，保证多线程的时候不串
    private HttpServletRequest httpServletRequest;


    //用户登录模块
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telephone") String telephone,
                                  @RequestParam(name="password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telephone)||
                org.apache.commons.lang3.StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登录服务，校验用户登录是否合法
        UserModel userModel = userService.validateLogin(telephone,this.EncodeByMD5(password));

        //将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);

    }

    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    //用户注册接口,传入用户手机号码和根据此号码生成的验证码以及用户的信息
    //这些信息会封装到userVO中
    public CommonReturnType register (
            @RequestParam(name="telephone") String telephone,
            @RequestParam(name="otpCode") String otpCode,
            @RequestParam(name="name") String name,
            @RequestParam(name="password") String password,
            @RequestParam(name="gender") Integer gender,
            @RequestParam(name="age") Integer age) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //验证手机号和对应的otpcode是否相符
        //在生成otpcode时，将验证码放入了session中，根据注册时传入的手机号，取对应的otpCode
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telephone);
        //将取出的otp和注册时的otp进行比对
        //使用alibaba的druid中的StringUtils工具类，equals方法本身有一个判空处理
        if(!StringUtils.equals(otpCode,inSessionOtpCode)){
            //抛自定义的异常，这里使用了自定义错误信息覆盖原错误信息。
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短息验证码不匹配");
        }

        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelephone(telephone);
//        userModel.setRegistMode("byPhone");
        //密码进行MD5加密
        userModel.setEncrptPassword(this.EncodeByMD5(password));

        userService.register(userModel);

        return CommonReturnType.create(null);

    }

    //对密码进行MD5加密修改
    public String EncodeByMD5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密字符串
        String newStr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }


    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
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
