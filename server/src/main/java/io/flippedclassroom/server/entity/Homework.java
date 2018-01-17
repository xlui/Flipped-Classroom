package io.flippedclassroom.server.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 课后作业
 */
@Entity
public class Homework implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	private String content;     // 作业内容
	private String answer;      // 作业答案

	// 课后作业与课程多对一关系
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "course_id")
	private Course course;

	public Homework() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
}
