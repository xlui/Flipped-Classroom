package io.flippedclassroom.server.entity;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 课前预习资料
 */
@ApiModel(value = "课前预习的资料", description = "与电子资料存放在不同位置")
@Entity
public class Preview implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	private String position;    // 资料上传位置

	// 课前预习资料与课程的多对一关系
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "course_id")
	private Course course;

	public Preview() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
}
