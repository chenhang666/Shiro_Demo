package com.shiro.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
* @Description: PermissionMapper
* @author chenhang
* @date 2019年6月13日
*
*/
@Mapper
public interface PermissionMapper {
	
	 List<String> getPermissionByRoleName(String roleName);

}
