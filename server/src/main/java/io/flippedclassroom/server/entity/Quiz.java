package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 随堂测试题
 */
@ApiModel(value = "随堂测试", description = "与课后作业不同")
@Entity
public class Quiz implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	private String content;     // 题目内容
	private String answer;      // 题目答案

	// 随堂测试与课程的多对一关系
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "course_id")
	@JsonIgnore
	private Course course;

	public Quiz() {
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
