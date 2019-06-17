package com.shiro.bean;

/**
* @Description: 权限类
* @author chenhang
* @date 2019年6月13日
*
*/
public class Permission {

	private int id;
	private String roleName;
	private String permission;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
}
