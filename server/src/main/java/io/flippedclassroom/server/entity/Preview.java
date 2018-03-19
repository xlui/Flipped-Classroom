package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

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
	private String date;
	private String size;
	private String author;

	// 课前预习资料与课程的多对一关系
	@ManyToOne
	@JoinColumn(name = "course_id")
	@JsonIgnore
	private Course course;

	public Preview() {
		super();
		this.date = LocalDate.now().toString();
	}

	public Preview(String position) {
		this.position = position;
		this.date = LocalDate.now().toString();
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
