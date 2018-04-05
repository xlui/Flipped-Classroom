package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.flippedclassroom.server.util.DateUtils;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 课程评论
 */
@ApiModel(value = "课程评论实体类")
@Entity
public class Comment implements Serializable {
	@Id
	@GeneratedValue
	@JsonProperty(access = JsonProperty.Access.AUTO)
	private Long id;
	@JsonProperty(access = JsonProperty.Access.AUTO)
	private String content;
	@JsonProperty(access = JsonProperty.Access.AUTO)
	private String date;
	@JsonProperty(access = JsonProperty.Access.AUTO)
	private Long reply = -1L;

	// 课程评论与用户的多对一关系，单向关系
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.AUTO)
	private User user;

	// 课程评论与课程的多对一关系
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Course course;

	public Comment() {
		super();
		this.date = DateUtils.currentTime();
	}

	public Comment(String content) {
		this.content = content;
		this.date = DateUtils.currentTime();
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getReply() {
		return reply;
	}

	public void setReply(Long reply) {
		this.reply = reply;
	}
}
