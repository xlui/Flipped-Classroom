package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "角色实体类")
@Entity
public class Role implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	private String role;    // 角色名

	// 角色与用户的一对多关系
	@OneToMany(mappedBy = "role")
	@JsonIgnore
	private List<User> userList;

	// 角色与权限的多对多关系
	@ManyToMany
	@JoinTable(name = "t_role_permission", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "permission_id")})
	private List<Permission> permissionList;

	public Role() {
		super();
	}

	public Role(String role) {
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<Permission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}
}
