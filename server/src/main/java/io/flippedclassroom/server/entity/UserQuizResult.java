package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;

@ApiModel(value = "用户随堂测试答案表")
@Entity
public class UserQuizResult implements Serializable {
	@Id
	@GeneratedValue
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	@JsonProperty(access = JsonProperty.Access.AUTO)
	private String answer;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String rightAnswer;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private User user;

	@ManyToOne
	@JoinColumn(name = "quiz_id")
    @JsonIgnore
	private Quiz quiz;

	public UserQuizResult() {
		super();
	}

	public UserQuizResult(String answer, String rightAnswer, User user, Quiz quiz) {
		this.answer = answer;
		this.rightAnswer = rightAnswer;
		this.user = user;
		this.quiz = quiz;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(String rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
}
