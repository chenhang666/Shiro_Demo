package com.shiro.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
* @Description: RoleMapper
* @author chenhang
* @date 2019年6月13日
*
*/
@Mapper
public interface RoleMapper {

	List<String> getRoleByUserName(String username);
	
}
