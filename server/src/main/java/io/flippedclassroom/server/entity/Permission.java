package io.flippedclassroom.server.entity;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 权限控制，分为学生账户、教师账户、管理员账户
 */
@ApiModel(value = "权限实体类", description = "考虑依据权限类区分登录、未登录用户或管理员、普通用户，提供试用和管理功能")
@Entity
public class Permission implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	private String permission;      // 权限名

	// 权限与角色的多对多关系
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable(name = "t_role_permission", joinColumns = {@JoinColumn(name = "permission_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
	private List<Role> roleList;

	public Permission() {
		super();
	}

	public Permission(String permission) {
		this.permission = permission;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public void addRole(Role role) {
		this.roleList.add(role);
	}
}
