package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "用户实体类")
@Entity
public class User implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private String username;    // 用户名
	@Column(nullable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;    // 密码加盐 hash
	@JsonIgnore
	private String salt;        // 每个用户唯一的盐

	// 以下均可选
	private String nick_name;   // 昵称
	private String avatar;      // 头像文件地址
	private String gender;      // 性别
	private String signature;   // 个性签名

	// 用户与认证情况的一对一关系
	@OneToOne                                // 用户是关系的维护端
	@JoinColumn(name = "auth_id")           // 指定外键名称
	@Fetch(FetchMode.JOIN)                  // 会使用 left join 查询，只产生一条 sql 语句
	private Authentication authentication;

	// 用户与角色的多对一关系
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	@JsonIgnore
	private Role role;

	// 用户与课程的多对多关系，通过表 `t_user_course` 维持
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "t_user_course", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "course_id")})
	@JsonIgnore
	private List<Course> courseList;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<Comment> commentList;

	public User() {
		super();
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(String nick_name, String gender, String signature) {
		this.nick_name = nick_name;
		this.gender = gender;
		this.signature = signature;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Course> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
}
