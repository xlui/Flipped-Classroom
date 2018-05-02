package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private String content;     // 题目内容
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private String answer;      // 题目答案

	// 随堂测试与课程的多对一关系
	@ManyToOne
	@JoinColumn(name = "course_id")
	@JsonIgnore
	private Course course;

	public Quiz() {
		super();
	}

	public Quiz(String content, String answer) {
		this.content = content;
		this.answer = answer;
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
