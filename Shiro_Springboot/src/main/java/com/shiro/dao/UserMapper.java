package com.shiro.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @Description: userMapper
* @author chenhang
* @date 2019年6月12日
*
*/
@Mapper
public interface UserMapper {

	String getPasswordByUserName(@Param("username") String username);
}
