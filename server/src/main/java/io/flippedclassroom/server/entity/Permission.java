package io.flippedclassroom.server.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 权限控制，分为学生账户、教师账户、管理员账户
 */
@Entity
public class Permission implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	private String permission;      // 权限名

	// 权限与角色的多对一关系
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	public Permission() {
		super();
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
