package io.flippedclassroom.server.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 电子学习资料，考虑上传后保存资料的位置
 */
@Entity
public class EData implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	private String position;    // 资料保存位置

	// 电子资料与课程的多对一关系
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "course_id")
	private Course course;

	public EData() {
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
