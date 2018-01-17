package io.flippedclassroom.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/*
 * 实名认证信息，默认认证状态为 false
 * todo: 使用图片识别进行实名认证
 */
@Entity
public class Authentication implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	private String studentID;       // 学号
	private String major;           // 专业
	private Boolean state = false;  // 认证状态

	public Authentication() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}
}
