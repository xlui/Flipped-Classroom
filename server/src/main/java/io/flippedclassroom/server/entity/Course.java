package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.flippedclassroom.server.config.Const;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程信息
 */
@ApiModel(value = "课程实体类")
@Entity
public class Course implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	private String name;        // 课程名
	private String major;       // 课程所属专业
	private String picture;        // 课程图片
	private Long count = 0L;    // 参与课程人数
	private String code;        // 课程唯一代码
	private boolean quizEnable = false;

	// 课程与用户的多对多关系
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name = "t_user_course", joinColumns = {@JoinColumn(name = "course_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ApiModelProperty(hidden = true)
	private List<User> userList;

	// 课程与随堂测试一对多
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ApiModelProperty(hidden = true)
	private List<Quiz> quizList;

	// 课程与课后测试一对多
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ApiModelProperty(hidden = true)
	private List<Homework> homeworkList;

	// 课程与电子资料一对多
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ApiModelProperty(hidden = true)
	private List<EData> eDataList;

	// 课程与课前预习资料一对多
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ApiModelProperty(hidden = true)
	private List<Preview> previewList;

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ApiModelProperty(hidden = true)
	private List<Comment> commentList;

	public Course() {
		super();
	}

	public Course(String name, String major) {
		this.name = name;
		this.major = major;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public Long getCount() {
		return (long) userList.parallelStream().filter(user -> user.getRole().getRole().equals("student")).collect(Collectors.toList()).size();
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getCode() {
		return "xust" + this.id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<Quiz> getQuizList() {
		return quizList;
	}

	public void setQuizList(List<Quiz> quizList) {
		this.quizList = quizList;
	}

	public List<Homework> getHomeworkList() {
		return homeworkList;
	}

	public void setHomeworkList(List<Homework> homeworkList) {
		this.homeworkList = homeworkList;
	}

	public List<EData> geteDataList() {
		return eDataList;
	}

	public void seteDataList(List<EData> eDataList) {
		this.eDataList = eDataList;
	}

	public List<Preview> getPreviewList() {
		return previewList;
	}

	public void setPreviewList(List<Preview> previewList) {
		this.previewList = previewList;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public String getPicture() {
		if (picture == null) {
			picture = Const.coursePictureLink.replace("COURSEID", this.id.toString());
		}
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public boolean isQuizEnable() {
		return quizEnable;
	}

	public void setQuizEnable(boolean quizEnable) {
		this.quizEnable = quizEnable;
	}
}
