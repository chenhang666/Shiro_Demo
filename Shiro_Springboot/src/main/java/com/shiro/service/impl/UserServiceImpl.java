package com.shiro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiro.dao.UserMapper;
import com.shiro.service.UserService;

/**
 * @Description: user业务层
 * @author chenhang
 * @date 2019年6月12日
 *
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getPasswordByUserName(String username) {
        String password = userMapper.getPasswordByUserName(username);
        return password;
    }

}
