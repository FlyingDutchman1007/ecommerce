package com.ecommerce.service;

import com.ecommerce.service.model.UserModel;
import org.springframework.stereotype.Service;

public interface UserService {
    // get user object by user id
    UserModel getUserById(Integer id);
}
