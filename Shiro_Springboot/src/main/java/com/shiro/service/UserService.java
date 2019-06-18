package com.shiro.service;

import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: user业务接口
 * @author chenhang
 * @date 2019年6月12日
 *
 */
@Mapper
public interface UserService {

    String getPasswordByUserName(String username);

}
