package io.flippedclassroom.server.entity.im;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.flippedclassroom.server.config.Const;
import io.flippedclassroom.server.entity.User;

import javax.persistence.*;

@Entity
public class Message {
	@Id
	@GeneratedValue
	@JsonProperty(access = JsonProperty.Access.AUTO)
	private Long id;
	@JsonProperty(access = JsonProperty.Access.AUTO)
	private String content;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String date;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private MessageType messageType;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.AUTO)
	private User user;

	public Message() {
		super();
	}

	public Message(String content, MessageType messageType) {
		this.content = content;
		this.messageType = messageType;
		this.date = Const.currentTime();
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
}
