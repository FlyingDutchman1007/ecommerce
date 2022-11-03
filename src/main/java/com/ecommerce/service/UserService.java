package com.ecommerce.service;

import com.ecommerce.error.BusinessException;
import com.ecommerce.service.model.UserModel;
import org.springframework.stereotype.Service;

public interface UserService {
    // get user object by user id
    UserModel getUserById(Integer id);

    // user register
    void register(UserModel userModel) throws BusinessException;

    // check login
    UserModel  validateLogin(String telephone, String password) throws BusinessException;
}
